package com.example.blog.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
  @Bean
  OpenAPI openApi() {
    return new OpenAPI()
    .info(
      new Info().title("Blog-App Api")
        .description("RestAPI for Blog App")
        .version("v0.0.1-snapshot")
    )
    .servers(
      List.of(
        new Server().url("http://localhost:8080").description("local dev")
      )
    )
    .components(
      new Components()
      .addSecuritySchemes(
        "accessToken",
        new SecurityScheme()
          .type(SecurityScheme.Type.HTTP)
          .scheme("bearer")
          .bearerFormat("JWT")
          .in(SecurityScheme.In.HEADER)
          .name("Authorization")
      )
      .addSecuritySchemes(
        "refreshToken",
        new SecurityScheme()
          .type(SecurityScheme.Type.HTTP)
          .scheme("bearer")
          .bearerFormat("JWT")
          .in(SecurityScheme.In.HEADER)
          .name("Authorization")
      )
    );
  }
}
