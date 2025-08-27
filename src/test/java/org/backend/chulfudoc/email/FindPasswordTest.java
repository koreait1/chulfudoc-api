package org.backend.chulfudoc.email;

import org.backend.chulfudoc.member.services.FindService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class FindPasswordTest {

    @Autowired
    private FindService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("비밀번호 초기화 및 초기화된 메일 이메일 전송 테스트")
    void resetTest() {
        assertDoesNotThrow(() -> service.reset("chtkdldjs43","chtkdldjs43@naver.com"));
        System.out.println();
    }

    @Test
    void test1() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();

        mockMvc.perform(post("/api/v1/member/findpw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"chtkdldjs43\", \"email\":\"chtkdldjs43@naver.com\"}"))
                .andDo(print());

    }
}

