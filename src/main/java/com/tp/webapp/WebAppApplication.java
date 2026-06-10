package com.tp.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class, args);
    }

    @GetMapping("/api/status")
    public String status() {
        return "{\"status\":\"UP\",\"app\":\"web-app\",\"version\":\"1.0.0\"}";
    }

    @GetMapping("/api/hello")
    public String hello() {
        return "{\"message\":\"Hello from TP Pipeline CI/CD!\"}";
    }
}
