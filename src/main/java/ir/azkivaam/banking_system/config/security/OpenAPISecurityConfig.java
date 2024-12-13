package ir.azkivaam.banking_system.config.security;

/*
 * @author masoome.aghayari
 * @since 12/29/23
 */

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPISecurityConfig {

    private static final String SCHEME_NAME = "bearerAuth";
    private static final String SCHEME = "bearer";

    @Value("${application.version}")
    private String appVersion;

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
                .components(new Components()
                                    .addSecuritySchemes(SCHEME_NAME, createBearerScheme()))
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                .info(new Info().title("Banking System")
                                .description("A mini project providing a simple demo for simple bank APIs!")
                                .version(appVersion));
    }

    private SecurityScheme createBearerScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme(SCHEME);
    }
}