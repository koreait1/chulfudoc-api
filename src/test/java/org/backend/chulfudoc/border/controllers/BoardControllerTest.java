package org.backend.chulfudoc.border.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.backend.chulfudoc.board.controllers.RequestBoard;
import org.backend.chulfudoc.board.repositories.BoardRepository;
import org.backend.chulfudoc.board.services.BoardInfoService;
import org.backend.chulfudoc.member.constants.Authority;
import org.backend.chulfudoc.member.test.libs.MockMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BoardRepository repository;
    @Autowired
    private BoardInfoService infoService;
    @Autowired
    private ObjectMapper om;

    @BeforeEach
    @MockMember(
            userID = "user01",
            email = "testUser@test.org",
            authority = Authority.MEMBER
    )
    public void init() throws Exception {
        RequestBoard form1 = new RequestBoard();
        RequestBoard form2 = new RequestBoard();
        form1.setSeq(1L);
        form1.setBid("notice");
        form1.setGid("12345315");
        form1.setPoster("user01");
        form1.setSubject("제목1");
        form1.setContent("내용1");
        form1.setNotice(true);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/board/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(form1)));

        form2.setSeq(2L);
        form2.setBid("freeTalk");
        form2.setGid("1234315");
        form2.setPoster("user01");
        form2.setSubject("제목2");
        form2.setContent("내용2");
        form2.setNotice(false);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/board/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(form2)));


    }

    @Test
    @MockMember( // ← 여기 붙임
            userID = "user01",
            email = "testUser@test.org",
            authority = Authority.MEMBER
    )
    void test1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/board/mypage/search")
                        .param("puuid", "sdfs-sdfs-qwer-wert")
                        .param("page", "1")
                        .param("limit", "20"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2));
    }
}