package com.kwy.cafebom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CafeBomApplication {

    public static void main(String[] args) {
        SpringApplication.run(CafeBomApplication.class, args);
    }



}
