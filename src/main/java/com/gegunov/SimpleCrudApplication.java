package com.gegunov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SimpleCrudApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimpleCrudApplication.class, args);
    }

}
