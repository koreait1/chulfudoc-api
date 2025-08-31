package org.backend.chulfudoc.member.services;

import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.global.email.entities.EmailMessage;
import org.backend.chulfudoc.global.email.services.EmailSendService;
import org.backend.chulfudoc.global.libs.Utils;
import org.backend.chulfudoc.member.controllers.RequestFindId;
import org.backend.chulfudoc.member.controllers.RequestFindPw;
import org.backend.chulfudoc.member.entities.Member;
import org.backend.chulfudoc.member.exceptions.MemberNotFoundException;
import org.backend.chulfudoc.member.repositories.MemberRepository;
import org.backend.chulfudoc.member.validators.FindValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FindService {

    private final FindValidator validator;
    private final MemberRepository repository;
    private final EmailSendService sendService;
    private final PasswordEncoder encoder;
    private final Utils utils;

    public void process(RequestFindPw form, Errors errors) {
        System.out.println("프로세스 시작");
        validator.validate(form, errors);
        if (errors.hasErrors()) { // 유효성 검사 실패시에는 처리 중단
            return;
        }

        // 비밀번호 초기화
        reset(form.userId(), form.email());
    }

    public void reset(String userId,String email) {
        /* 비밀번호 초기화 S */
        Member member = repository.findByUserIdAndEmail(userId, email).orElseThrow(MemberNotFoundException::new);

        String newPassword = utils.randomChars(12); // 초기화 비밀번호는 12자로 생성
        member.setPassword(encoder.encode(newPassword));

        repository.saveAndFlush(member);

        /* 비밀번호 초기화 E */

        EmailMessage emailMessage = new EmailMessage(
                email,
                utils.getMessage("Email.password.reset"),
                utils.getMessage("Email.password.reset"));
        Map<String, Object> tplData = new HashMap<>();
        tplData.put("password", newPassword);
        sendService.sendEmail(emailMessage, "password_reset", tplData);
    }


    /* 아이디 조회 */
    public String process(RequestFindId form, Errors errors) {
        validator.validate(form, errors);
        if (errors.hasErrors()) { // 유효성 검사 실패 시 중단
            return null;
        }
        String name  = form.name().trim();
        String email = form.email().trim();

        Member member = repository.findByNameAndEmail(name, email)
                .orElseThrow(MemberNotFoundException::new);

        return member.getUserId();
    }
}
