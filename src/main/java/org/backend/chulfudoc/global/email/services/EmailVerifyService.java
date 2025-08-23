package org.backend.chulfudoc.global.email.services;

import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.global.email.entities.EmailMessage;
import org.backend.chulfudoc.global.libs.Utils;
import org.backend.chulfudoc.member.libs.MemberUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailVerifyService {

    private final Utils utils;
    private final EmailSendService service;
    private final MemberUtil memberUtil;
    private final RedisTemplate<String, Integer> redisTemplate;

    // 인증 번호 생성 및 발송
    public boolean sendCode(String email) {
        String key = memberUtil.getUserHash(); // 비회원 시도 : randomUuid || 회원 시도 : PUUID

        // key 값 확인 용
        System.out.println("EmailVerifyService Key : " + key);
        int authNum;

        do {
            authNum = (int)(Math.random() * 1000000); // 0 ~ 999999
        } while (authNum < 100000);


        // redis에 User-Hash & 생성한 인증번호 저장
        redisTemplate.opsForValue().set(key, authNum, 3, TimeUnit.MINUTES);

        // redis에 저장한 인증번호 조회 (Test를 위해 임시 작성) S
        Integer result = redisTemplate.opsForValue().get(key);

        if (result != null) {
            System.out.println("조회된 인증번호 값: " + result);
        } else {
            System.out.println("값 없음");
        }
        // redis에 저장한 인증번호 조회 (Test를 위해 임시 작성) E

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

        String hash = memberUtil.getUserHash();
        // 인증번호 값 가져오기
        Integer authNum = redisTemplate.opsForValue().get(hash);

        if (authNum != null) {
            System.out.println("인증 번호: " + authNum);

            return code == authNum;
        } else {
            System.out.println("인증번호 없음(기한 만료)");

            return false;
        }
    }
}
