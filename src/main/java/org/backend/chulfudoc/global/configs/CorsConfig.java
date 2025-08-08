package org.backend.chulfudoc.global.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.addAllowedOrigin("*");
        // config.setAllowCredentials(true); 브라우저가 Ajax, fetch 요청 시 쿠키·세션·Authorization 헤더 같은 인증 정보를 포함할 수 있도록

        source.registerCorsConfiguration("/api/v1/**", config);

        return new CorsFilter(source);
    }
}
