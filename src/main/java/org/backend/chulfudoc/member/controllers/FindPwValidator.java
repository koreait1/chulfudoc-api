package org.backend.chulfudoc.member.controllers;

import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.member.repositories.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class FindPwValidator implements Validator {
    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestFindPw.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestFindPw form = (RequestFindPw) target;
        String email = form.email();
        String userId = form.userId();

        if (StringUtils.hasText(email) && StringUtils.hasText(userId) && !memberRepository.existsByUserIdAndEmail(userId, email)) {
            errors.reject("NotFound.member");
        }
    }
}
