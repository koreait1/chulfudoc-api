package org.backend.chulfudoc.member.services;

import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.member.MemberInfo;
import org.backend.chulfudoc.member.entities.Member;
import org.backend.chulfudoc.member.repositories.MemberRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {

    private final MemberRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = repository.findByUserId(username).orElseThrow(() -> new UsernameNotFoundException(username));

        return MemberInfo.builder()
                .member(member)
                .build();
    }

}
