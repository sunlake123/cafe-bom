package com.kwy.cafebom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CafeBomApplication {

    public static void main(String[] args) {
        SpringApplication.run(CafeBomApplication.class, args);
    }

}
