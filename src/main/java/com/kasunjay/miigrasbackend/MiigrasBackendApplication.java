package com.kasunjay.miigrasbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class MiigrasBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiigrasBackendApplication.class, args);
    }

}
