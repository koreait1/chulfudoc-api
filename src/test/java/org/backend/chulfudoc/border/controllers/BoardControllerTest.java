package org.backend.chulfudoc.border.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.backend.chulfudoc.board.controllers.RequestBoard;
import org.backend.chulfudoc.board.repositories.BoardRepository;
import org.backend.chulfudoc.board.services.BoardInfoService;
import org.backend.chulfudoc.member.test.libs.MockMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MockMember member;
    @Autowired
    private BoardRepository repository;
    @Autowired
    private BoardInfoService infoService;
    @Autowired
    private ObjectMapper om;

    RequestBoard form1 = new RequestBoard();
    RequestBoard form2 = new RequestBoard();
    @BeforeEach
    public void init() throws Exception {
        form1.setSeq(1L);
        form1.setBid("notice");
        form1.setGid("12345315");
        form1.setPoster("user01");
        form1.setSubject("제목1");
        form1.setContent("내용1");
        form1.setNotice(true);
        form2.setSeq(2L);
        form2.setBid("freeTalk");
        form2.setGid("1234315");
        form2.setPoster("user01");
        form2.setSubject("제목2");
        form2.setContent("내용2");
        form2.setNotice(false);



    }

    @Test
    void test1() throws Exception{
        String body = om.writeValueAsString(form1);
        String body2 = om.writeValueAsString(form2);
        mockMvc.perform(post("/api/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .content(body2))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
