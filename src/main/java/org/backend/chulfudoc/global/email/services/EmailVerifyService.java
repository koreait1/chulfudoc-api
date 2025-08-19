package org.backend.chulfudoc.global.email.services;

import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.global.email.entities.EmailMessage;
import org.backend.chulfudoc.global.email.entities.EmailSession;
import org.backend.chulfudoc.global.email.repositories.EmailSessionRepository;
import org.backend.chulfudoc.global.libs.Utils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerifyService {

    private final Utils utils;
    private final EmailSendService service;
    private final EmailSessionRepository repository;

    // 인증 번호 생성
    public boolean sendCode(String email) {
        int authNum = (int)(Math.random() * 99999);

        // redis에 생성한 인증번호 저장
        EmailSession session = new EmailSession();
        session.setKey("EmailAuthNum");
        session.setValue(authNum);
        repository.save(session);

        // redis에 저장한 인증번호 조회
        Optional<EmailSession> result = repository.findById("EmailAuthNum");

        if (result.isPresent()) {
            EmailSession session2 = result.get();
            System.out.println("조회된 인증번호 값: " + session2.getValue());
        } else {
            System.out.println("값 없음");
        }

        // 공통 메시지 관리에서 제목과 메일 내용을 가져와 메시지 VO값에 대입
        // 회원가입 이메일 인증메일입니다. | 발급된 인증코드를 회원가입 목록에 입력하세요.
        EmailMessage emailMessage = new EmailMessage(
                email,
                utils.getMessage("Email.verification.subject"),
                utils.getMessage("Email.verification.message"));

        // 이메일은 템플릿 형태를 참고해서 전송
        Map<String, Object> tplData = new HashMap<>();
        tplData.put("authNum", authNum);

        return service.sendEmail(emailMessage, "auth", tplData);
    }

    // 인증번호 일치 여부 체크
    public boolean check(int code) {

        // 인증번호 값 가져오기
        Optional<EmailSession> session = repository.findById("EmailAuthNum");

        if (session.isPresent()) {
            EmailSession authNum = session.get();
            System.out.println("인증 번호: " + authNum.getValue());

            return code == authNum.getValue();
        } else {
            System.out.println("인증번호 없음(기한 만료)");

            return false;
        }
    }
}
