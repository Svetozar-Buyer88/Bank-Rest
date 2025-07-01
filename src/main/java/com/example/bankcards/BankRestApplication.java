package com.example.bankcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(
        exclude = { LiquibaseAutoConfiguration.class }
)
@EnableScheduling
//@EntityScan("com.example.bankcards.entity")         // <— где лежат ваши @Entity
//@EnableJpaRepositories("com.example.bankcards.repository") // <— где лежат ваши репозитории

public class BankRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankRestApplication.class, args);
    }
}

