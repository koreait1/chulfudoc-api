package org.backend.chulfudoc.member.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.file.services.FileInfoService;
import org.backend.chulfudoc.global.search.ListData;
import org.backend.chulfudoc.global.search.Pagination;
import org.backend.chulfudoc.member.MemberInfo;
import org.backend.chulfudoc.member.constants.Authority;
import org.backend.chulfudoc.member.controllers.MemberSearch;
import org.backend.chulfudoc.member.entities.Member;
import org.backend.chulfudoc.member.entities.QMember;
import org.backend.chulfudoc.member.repositories.MemberRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Lazy
@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {

    private final MemberRepository repository;
    private final HttpServletRequest request;
    private final FileInfoService fileInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = repository.findByUserId(username).orElseThrow(() -> new UsernameNotFoundException(username));

        addInfo(member); // 추가 정보 처리

        return MemberInfo.builder()
                .member(member)
                .build();
    }

    /**
     * 회원정보 추가 처리
     *
     * @param member
     */
    private void addInfo(Member member) {
        String gid = member.getGid();
        if (gid != null) {
            member.setProfileImage(fileInfoService.get(gid));
        }
    }

    public ListData<Member> getMemberList(MemberSearch search){
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 10 : limit; // 테스트르 위해 일시적으로 페이지에 표시할 회원 목록 수 줄임 -> 배포 시 수정

        /* 쿼리 DSL S */
        QMember member = QMember.member;
        BooleanBuilder andBuilder = new BooleanBuilder();

        String searchOption = search.getSopt();
        String searchKey = search.getSkey();

        // 검색 옵션이 없을 경우 모든 회원 조회
        searchOption = StringUtils.hasText(searchOption) ? searchOption.toUpperCase() : "ALL";
        // 검색 키워드가 있을 경우 검색 옵션에 알맞게 검색 ex) 이름 옵션 -> 이름 중 검색 키워드에 해당하는 회원 검색
        if (StringUtils.hasText(searchKey)) {
            searchKey = searchKey.trim();
            StringExpression fields = null;
            if (searchOption.equals("NAME")) {
                fields = member.name;
            } else if (searchOption.equals("EMAIL")) {
                fields = member.email;
            } else if (searchOption.equals("MOBILE")) {
                fields = member.mobile;
            } else if (searchOption.equals("ID")) {
                fields = member.userId;
            } else {
                fields = member.name.concat(member.email)
                        .concat(member.mobile).concat(member.userId);
            }
            andBuilder.and(fields.contains(searchKey));
        }
        /* 쿼리 DSL E */

        // 검색 조건 중 마지막에 권한에 따른 조회 설정 || search 객체 내부에 권한이 설정되어 있으면 해당 권한에 속하는 회원만 조회
        List<Authority> authorities = search.getAuthorities();
        if (authorities != null && !authorities.isEmpty()) {
            andBuilder.and(member.authority.in(authorities));
        }

        // 페이징
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
        Page<Member> data = repository.findAll(andBuilder, pageable);

        List<Member> items = data.getContent();
        long total = data.getTotalElements();

        Pagination pagination = new Pagination(page, (int)total, 10, limit, request);

        return new ListData<>(items, pagination);
    }
}



