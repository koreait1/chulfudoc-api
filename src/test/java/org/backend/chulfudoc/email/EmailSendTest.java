package org.backend.chulfudoc.email;

import org.backend.chulfudoc.global.email.entities.EmailMessage;
import org.backend.chulfudoc.global.email.services.EmailSendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailSendTest {

    @Autowired
    private EmailSendService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test1(){
        EmailMessage message = new EmailMessage("tailred215@gmail.com", "메일보내기 테스트...", "성공...");
        Map<String,Object> tplData = new HashMap<>();
        tplData.put("authNum", "123456");
        System.out.println(tplData.get("authNum"));

        boolean success = service.sendEmail(message, "auth", tplData);

        assertTrue(success);
    }
}
