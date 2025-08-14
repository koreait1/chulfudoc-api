package org.backend.chulfudoc.member.jwt;

import org.backend.chulfudoc.member.controllers.RequestJoin;
import org.backend.chulfudoc.member.services.JoinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles({"default", "test"})
public class TokenServiceTest {

    @Autowired
    private JoinService joinService;

    @Autowired
    private TokenService tokenService;

    @BeforeEach
    void init() {
        RequestJoin form = new RequestJoin();
        form.setUserId("user01");
        form.setEmail("user01@test.org");
        form.setPassword("!aA123456");
        form.setConfirmPassword(form.getPassword());
        form.setMobile("01010001000");
        form.setName("사용자01");
        form.setTermsAgree(true);

        joinService.process(form);
        System.out.println("Join 테스트 성공");
    }

    @Test
    @DisplayName("토큰 발급 테스트")
    void jwtCreationTest() {
        String token = tokenService.create("user01");
        System.out.println(token);

        assertNotNull(token);
    }

}