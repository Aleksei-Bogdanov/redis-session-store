package com.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisSessionStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisSessionStoreApplication.class, args);
    }
}