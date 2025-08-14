package org.backend.chulfudoc.member.test.libs;

import org.backend.chulfudoc.member.MemberInfo;
import org.backend.chulfudoc.member.entities.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.LocalDateTime;

public class MockSecurityContextFactory implements WithSecurityContextFactory<MockMember> {

    @Override
    public SecurityContext createSecurityContext(MockMember annotation) {

        Member member = new Member();
        member.setPUUID(annotation.PUUID());
        member.setUserId(annotation.userID());
        member.setEmail(annotation.email());
        member.setPassword(annotation.password());
        member.setName(annotation.name());
        member.setMobile(annotation.mobile());
        member.setAuthority(annotation.authority());
        member.setCredentialChangedAt(LocalDateTime.now().plusMonths(1L));

        MemberInfo memberInfo = MemberInfo.builder()
                .member(member)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(memberInfo, null, memberInfo.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication); // 로그인 처리

        return context;
    }

}
