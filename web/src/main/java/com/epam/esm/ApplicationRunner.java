package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
public class ApplicationRunner {

    /**
     * The entry point of Spring Boot application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ApplicationRunner.class, args);
    }
}
