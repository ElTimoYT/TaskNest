package com.eltimo.tasknest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TasknestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TasknestApplication.class, args);
    }

}
