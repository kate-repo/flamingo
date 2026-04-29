package com.assignment.flamingo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Kate
 * @since 23-Apr-2026
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {
        "com.assignment.flamingo.api"
})
public class GameSessionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameSessionServiceApplication.class, args);
    }

}
