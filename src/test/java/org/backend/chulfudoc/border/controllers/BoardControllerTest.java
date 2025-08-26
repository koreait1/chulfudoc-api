package org.backend.chulfudoc.border.controllers;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.backend.chulfudoc.board.controllers.BoardSearch;
import org.backend.chulfudoc.board.controllers.RequestBoard;
import org.backend.chulfudoc.board.entities.Board;
import org.backend.chulfudoc.board.entities.BoardData;
import org.backend.chulfudoc.board.entities.QBoardData;
import org.backend.chulfudoc.board.repositories.BoardDataRepository;
import org.backend.chulfudoc.board.services.BoardInfoService;
import org.backend.chulfudoc.global.search.ListData;
import org.backend.chulfudoc.global.search.Pagination;
import org.backend.chulfudoc.member.services.MemberInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * getList(BoardSearch) 서비스 로직에 대한 SpringBootTest.
 * - 단일 bid일 때 rowsForPage로 limit 보정
 * - sopt/skey 키워드 검색(ALL, SUBJECT/CONTENT/SUBJECT_CONTENT/NAME)
 * - 날짜 범위(sDate, eDate)
 * - userId 필터(member.userId IN ...)
 * - 정렬/페이징 및 count
 */
@SpringBootTest
class BoardGetList {

    @Autowired
    private BoardInfoService boardInfoService; // ← 실제 서비스 빈 이름/타입으로 교체

    @MockBean
    private MemberInfoService configInfoService;

    @MockBean
    private JPAQueryFactory queryFactory;

    @MockBean
    private BoardDataRepository boardDataRepository;

    // QueryDSL 체인용 목
    @SuppressWarnings("unchecked")
    private final JPAQuery<BoardData> jpaQueryMock = mock(JPAQuery.class);

    private final QBoardData b = QBoardData.boardData;

    @BeforeEach
    void setup() {
        // 서비스 내부에서 Pagination 생성 시 request를 참조한다면 필드 주입 필요
        ReflectionTestUtils.setField(boardInfoService, "request", new MockHttpServletRequest());

        // QueryDSL 체인 세팅(필요 최소)
        when(queryFactory.selectFrom(b)).thenReturn(jpaQueryMock);
        when(jpaQueryMock.fetchJoin()).thenReturn(jpaQueryMock);
        when(jpaQueryMock.where(any(BooleanBuilder.class))).thenReturn(jpaQueryMock);
        when(jpaQueryMock.offset(anyLong())).thenReturn(jpaQueryMock);
        when(jpaQueryMock.limit(anyLong())).thenReturn(jpaQueryMock);
        when(jpaQueryMock.orderBy(any(OrderSpecifier.class), any(OrderSpecifier.class))).thenReturn(jpaQueryMock);
    }

    @Test
    @DisplayName("단일 bid이면 rowsForPage로 limit 보정되고 페이징/정렬/조회 정상")
    void singleBid_usesBoardRowsForPage() {
        // given
        BoardSearch search = new BoardSearch();
        search.setBid(List.of("freetalk")); // 단일 bid → rowsForPage 적용
        search.setPage(2);
        search.setLimit(0);                 // 전달값이 0이어도 rowsForPage로 대체 기대

        Board boardCfg = new Board();
        boardCfg.setRowsForPage(15);
        boardCfg.setPageCount(7);

        RequestBoard form = new RequestBoard();
        form.setSeq(1L);
        form.setPoster("user01");
        form.setSubject("제목1");
        form.setContent("내용1");
        form.setNotice(true);
        RequestBoard form1 = new RequestBoard();
        form1.setSeq(2L);
        form1.setPoster("user02");
        form1.setSubject("제목2");
        form1.setContent("내용2");
        form1.setNotice(false);
        when(boardDataRepository.count(any(BooleanBuilder.class))).thenReturn(2L);

        ArgumentCaptor<BooleanBuilder> whereCaptor = ArgumentCaptor.forClass(BooleanBuilder.class);

        // when
        ListData<BoardData> out = boardInfoService.getList(search);

        // then: 데이터
        assertThat(out).isNotNull();
        assertThat(out.getItems()).hasSize(2);
        Pagination p = out.getPagination();
        assertThat(p.getPage()).isEqualTo(2);
        assertThat(p.getLimit()).isEqualTo(15);     // rowsForPage로 보정 확인
        assertThat(p.getRange()).isEqualTo(7);      // pageCount 적용 확인

        // then: QueryDSL 체인 호출 검증
        verify(queryFactory).selectFrom(b);
        verify(jpaQueryMock).leftJoin(b.member);
        verify(jpaQueryMock).fetchJoin();
        verify(jpaQueryMock).where(whereCaptor.capture());
        verify(jpaQueryMock).offset(15 * (2 - 1));  // offset = (page-1)*limit = 15
        verify(jpaQueryMock).limit(15);
        verify(jpaQueryMock).orderBy(b.notice.desc(), b.createdAt.desc());
        verify(jpaQueryMock).fetch();
        verify(boardDataRepository).count(whereCaptor.getValue());

        // where 조건에 bid가 포함되었는지 간단 확인(toString 기반)
        String whereStr = String.valueOf(whereCaptor.getValue());
        assertThat(whereStr).contains("freetalk"); // QueryDSL toString 표현에 값이 들어감(구현체마다 다를 수 있음)
    }

    @Test
    @DisplayName("키워드 검색: sopt=ALL 일 때 subject+content+name 통합 contains")
    void keywordSearch_all() {
        // given
        BoardSearch search = new BoardSearch();
        search.setBid(List.of("notice", "freetalk"));
        search.setSkey("헬로");
        // sopt 미지정 → ALL로 처리

        when(jpaQueryMock.fetch()).thenReturn(List.of());
        when(boardDataRepository.count(any(BooleanBuilder.class))).thenReturn(0L);

        ArgumentCaptor<BooleanBuilder> whereCaptor = ArgumentCaptor.forClass(BooleanBuilder.class);

        // when
        ListData<BoardData> out = boardInfoService.getList(search);

        // then
        assertThat(out.getItems()).isEmpty();

        verify(jpaQueryMock).where(whereCaptor.capture());
        String s = String.valueOf(whereCaptor.getValue());
        // 통합검색이므로 skey, bid 일부가 where 문자열에 포함되는지 확인
        assertThat(s).contains("헬로");      // contains(skey)
        assertThat(s).contains("notice");    // bid in (...)
        assertThat(s).contains("freetalk");
    }

    @Test
    @DisplayName("날짜 범위 + userId 필터(member.userId IN [...])")
    void dateRange_and_userIdFilter() {
        // given
        BoardSearch search = new BoardSearch();
        search.setSDate(LocalDate.of(2025, 8, 1));
        search.setEDate(LocalDate.of(2025, 8, 31));
        search.setUserId(List.of("user01", "user02"));
        search.setPage(1);
        search.setLimit(10);

        when(jpaQueryMock.fetch()).thenReturn(List.of());
        when(boardDataRepository.count(any(BooleanBuilder.class))).thenReturn(0L);

        ArgumentCaptor<BooleanBuilder> whereCaptor = ArgumentCaptor.forClass(BooleanBuilder.class);

        // when
        ListData<BoardData> out = boardInfoService.getList(search);

        // then
        assertThat(out.getItems()).isEmpty();
        Pagination p = out.getPagination();
        assertThat(p.getPage()).isEqualTo(1);
        assertThat(p.getLimit()).isEqualTo(10);

        // where 캡쳐 후, userId와 날짜 경계가 들어갔는지 확인(간단 문자열 기반)
        verify(jpaQueryMock).where(whereCaptor.capture());
        String s = String.valueOf(whereCaptor.getValue());
        assertThat(s).contains("user01").contains("user02"); // IN 조건
        assertThat(s).contains("createdAt");                 // 날짜 조건 존재
    }
}