package com.simbongsa.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.simbongsa.global.common.constant.TokenConstant.BEARER_TYPE;
import static com.simbongsa.global.common.constant.TokenConstant.JWT;
import static io.swagger.v3.oas.models.security.SecurityScheme.Type.*;
import static org.springframework.http.HttpHeaders.*;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(AUTHORIZATION, new SecurityScheme()
                                .name(AUTHORIZATION)
                                .type(HTTP)
                                .scheme(BEARER_TYPE)
                                .bearerFormat(JWT)))
                .addSecurityItem(new SecurityRequirement().addList(AUTHORIZATION))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Project of Simbongsa")
                .description("API Swagger UI")
                .version("1.0.0");
    }
}
