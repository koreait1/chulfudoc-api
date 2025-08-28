package org.backend.chulfudoc.member.repositories;

import org.backend.chulfudoc.member.constants.SocialChannel;
import org.backend.chulfudoc.member.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String>, QuerydslPredicateExecutor<Member> {
    Optional<Member> findByUserId(String userId);
    Optional<Member> findBySocialChannelAndSocialToken(SocialChannel channel, String socialToken);
    Optional<Member> findByUserIdAndEmail(String userId, String email);
    Optional<Member> findByNameAndEmail(String name, String email);

    boolean existsByEmail(String email);
    boolean existsByUserId(String userId);
    boolean existsByUserIdAndEmail(String userId, String email);
    boolean existsByNameAndEmail(String name, String email);
}