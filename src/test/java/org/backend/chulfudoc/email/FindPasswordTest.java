package org.backend.chulfudoc.email;

import org.backend.chulfudoc.member.services.FindPwService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class FindPasswordTest {

    @Autowired
    private FindPwService service;

    @Test
    @DisplayName("비밀번호 초기화 및 초기화된 메일 이메일 전송 테스트")
    void resetTest() {
        assertDoesNotThrow(() -> service.reset("chtkdldjs43","chtkdldjs43@naver.com"));
        System.out.println();
    }
}

