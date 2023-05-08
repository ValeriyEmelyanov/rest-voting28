package com.example.restvoting28.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@OpenAPIDefinition(
        info = @Info(
                title = "REST API documentation",
                version = "1.0",
                description = """
                        <h3>Restaurant voting application</h3>
                        <p><b>Тестовые параметры аутентификации:</b><br>
                        - user@gmail.com / password<br>
                        - admin@gmail.com / admin<br>
                        """,
                contact = @Contact(url = "https://github.com/ValeriyEmelyanov", name = "Valeriy Emelyanov", email = "emelva@rambler.ru")
        ),
        servers = {
                @Server(url = "http://localhost:8080")
        },
        security = @SecurityRequirement(name = "basicAuth")
)
@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("REST API")
                .pathsToMatch("/api/**")
                .build();
    }
}
