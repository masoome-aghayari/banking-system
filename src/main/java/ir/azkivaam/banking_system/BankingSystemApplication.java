package ir.azkivaam.banking_system;

import io.swagger.v3.oas.models.annotations.OpenAPI31;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@OpenAPI31
public class BankingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingSystemApplication.class, args);
    }

}
