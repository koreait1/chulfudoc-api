package org.backend.chulfudoc.member.services;

import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.member.repositories.MemberRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberDeleteService {

    private final MemberRepository repository;

    //매일 자정 탈퇴 처리된 지 30일이 지난 회원 DB에서 삭제
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredMembers() {
        repository.deleteAllByDeletedAtBefore(LocalDateTime.now().minusDays(30));
    }
}