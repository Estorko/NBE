package com.nbe.automation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.nbe.automation")
public class NbeAutomationSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(NbeAutomationSpringApplication.class, args);
    }
}
