package org.backend.chulfudoc.member.services;

import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.global.exceptions.BadRequestException;
import org.backend.chulfudoc.member.entities.Member;
import org.backend.chulfudoc.member.jwt.TokenService;
import org.backend.chulfudoc.member.libs.MemberUtil;
import org.backend.chulfudoc.member.repositories.MemberRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberDeleteService {

    private final MemberUtil memberUtil;
    private final MemberRepository memberRepository;
    private final TokenService tokenService; // 프로젝트에 이미 존재(필요시 주입만)

    //현재 로그인한 회원 삭제
    @Transactional
    public void deleteCurrentMember() {
        Member member = memberUtil.getMember();
        if (member == null) {
            throw new BadRequestException("인증이 필요합니다.");
        }
        //이미 탈퇴되어 있으면 예외
        if (member.getDeletedAt() != null) {
            throw new BadRequestException("이미 탈퇴 처리된 계정입니다.");
        }

        // 회원 탈퇴 처리
        member.setDeletedAt(LocalDateTime.now());
        memberRepository.saveAndFlush(member);

        SecurityContextHolder.clearContext();
    }
}