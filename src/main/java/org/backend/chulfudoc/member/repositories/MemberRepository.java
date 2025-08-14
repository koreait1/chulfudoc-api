package org.backend.chulfudoc.member.repositories;

import org.backend.chulfudoc.member.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {
    Optional<Member> findByUserId(String userId);
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByUserId(String userId);
}