package org.backend.chulfudoc.member.test.libs;

import org.backend.chulfudoc.member.constants.Authority;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockSecurityContextFactory.class)
public @interface MockMember {

    String puuid() default "sdfs-sdfs-qwer-wert";
    String userID() default "user01";
    String email() default "testUser@test.org";
    String password() default "_aA123456";
    String name() default "테스트 사용자";
    String mobile() default "01010001000";
    Authority authority() default Authority.MEMBER;


}