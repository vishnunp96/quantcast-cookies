package com.quantcast.cookies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Application class
 * Entrypoint to the application in AppRunner
 */

@SpringBootApplication
public class CookiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CookiesApplication.class, args);
    }

}
