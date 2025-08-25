package org.backend.chulfudoc.email;

import org.backend.chulfudoc.global.email.entities.EmailMessage;
import org.backend.chulfudoc.global.email.services.EmailSendService;
import org.backend.chulfudoc.global.email.services.EmailVerifyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailSendTest {

    @Autowired
    private EmailSendService service;

    @Autowired
    private EmailVerifyService emailVerifyService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    @Test
    void test1(){
        EmailMessage message = new EmailMessage("tailred215@gmail.com", "메일보내기 테스트...", "성공...");
        Map<String,Object> tplData = new HashMap<>();
        tplData.put("authNum", "123456");
        System.out.println(tplData.get("authNum"));

        boolean success = service.sendEmail(message, "auth", tplData);

        assertTrue(success);
    }

    @Test
    @DisplayName("EmailVerifyServiceTest")
    void test2(){
        boolean result = emailVerifyService.sendCode("tailred215@gmail.com");
        assertTrue(result);
    }

    @Test
    @DisplayName("EmailControllerTest")
    void test3() throws Exception{

        MockHttpServletRequest request = new MockHttpServletRequest();

        // API 요청 처리 (email 파라미터는 SQ에 심어서 요청 | 테스트를 위해 | 지정된 이메일로 인증 이메일 발급)
        mockMvc.perform(get("/api/v1/email/verify").param("email","tailred215@gmail.com")
                        .header("User-Hash", "1111"))
                .andDo(print()) // 요청/응답 정보를 콘솔에 출력
                .andExpect(status().isOk()) // 응답 상태 점검 | 2xx
                .andReturn().getResponse().getContentAsString();

        String key = "1111";

        Integer authNum = redisTemplate.opsForValue().get(key);

        // API 요청 처리 (인증번호 검증)
        mockMvc.perform(get("/api/v1/email/check")
                        .param("authNum", String.valueOf(authNum))
                        .header("User-Hash", "1111"))
                .andDo(print());
    }
}
