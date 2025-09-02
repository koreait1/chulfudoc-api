package org.backend.chulfudoc.member.validators;

import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.member.controllers.RequestFindId;
import org.backend.chulfudoc.member.controllers.RequestFindPw;
import org.backend.chulfudoc.member.repositories.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class FindValidator implements Validator {
    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return RequestFindPw.class.isAssignableFrom(clazz) || RequestFindId.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // 비밀번호 찾기 검증
        if (target instanceof RequestFindPw form) {
            String email = form.email();
            String userId = form.userId();

            if (StringUtils.hasText(email) && StringUtils.hasText(userId)
                    && !memberRepository.existsByUserIdAndEmail(userId, email)) {
                errors.reject("NotFound.member");
            }
            return;
        }

        // 아이디 찾기 검증
        if (target instanceof RequestFindId form) {
            String name  = form.name();   // 클래스로 만들었다면 getName()
            String email = form.email();  // 클래스로 만들었다면 getEmail()

            if (StringUtils.hasText(name) && StringUtils.hasText(email)
                    && !memberRepository.existsByNameAndEmail(name, email)) {
                errors.reject("NotFound.member");
            }
        }
    }
}
