package org.backend.chulfudoc.email;

import org.backend.chulfudoc.member.entities.Member;
import org.backend.chulfudoc.member.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // 시큐리티 필터 비활성화
@ActiveProfiles("test")
public class FindIdTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();

        // 테스트용 회원 데이터 저장
        Member m = new Member();
        // TODO: 엔터티 필드에 맞게 채우세요 (아래 3개는 반드시 들어가야 함)
        m.setUserId("chltkdwns");
        m.setName("김수정");
        m.setEmail("chtkdldjs43@naver.com");

        memberRepository.save(m);
    }

    @Test
    void test1() throws Exception {
        mvc.perform(get("/api/v1/member/findId")
                        .param("name", "김수정")
                        .param("email", "chtkdldjs43@naver.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) jsonPath("$.userId").value("chltkdwns"));
    }

    @Test
    void test2() throws Exception {
        mvc.perform(get("/member/findId")
                        .param("name", "아무나")
                        .param("email", "any@example.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect((ResultMatcher) jsonPath("$.messages.global[0]").exists());
    }
}
