package org.backend.chulfudoc.member.services;

import org.backend.chulfudoc.global.search.ListData;
import org.backend.chulfudoc.member.controllers.MemberSearch;
import org.backend.chulfudoc.member.entities.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberSearchTest {

    @Autowired
    private MemberInfoService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test1(){

        MemberSearch search = new MemberSearch();
        search.setPage(1);

        ListData<Member> data = service.getMemberList(search);
        System.out.println(data);
    }

    @Test
    void test2() throws Exception{
        MemberSearch search = new MemberSearch();
        mockMvc.perform(get("/api/v1/member/list")
                        .with(user("user01").authorities(() -> "ADMIN")))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
