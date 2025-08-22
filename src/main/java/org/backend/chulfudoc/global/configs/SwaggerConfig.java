package org.backend.chulfudoc.global.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info=@Info(title="철푸닥 API", license = @License(name="MIT"), contact = @Contact(email="kiana507680@gmail.com")))
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("철푸닥 API")
                .pathsToMatch("/api/v1/**")
                .build();
    }
}