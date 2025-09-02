package org.backend.chulfudoc.global.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Value("${cors.allowed-origin}")
    private String allowedOrigin;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.addAllowedOrigin(allowedOrigin);
        config.setAllowCredentials(true); //브라우저가 Ajax, fetch 요청 시 쿠키·세션·Authorization 헤더 같은 인증 정보를 포함할 수 있도록

        // CORS(Cross-Origin Resource Sharing) 정책 때문에 막히는 Ajax 요청을 허용하기 위해 사용
        source.registerCorsConfiguration("/api/v1/**", config);
        source.registerCorsConfiguration("/file/**", config);

        return new CorsFilter(source);
    }
}
