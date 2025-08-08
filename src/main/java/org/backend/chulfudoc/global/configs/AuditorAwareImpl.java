package org.backend.chulfudoc.global.configs;

import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.member.libs.MemberUtil;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    private final MemberUtil memberUtil;

    @Override
    public Optional<String> getCurrentAuditor() { // null값 처리를 위해 Optional - Object랑 헷갈리지 않기
        String email = memberUtil.isLogin() ? memberUtil.getMember().getEmail() : null;

        return Optional.ofNullable(email);
    }

}

