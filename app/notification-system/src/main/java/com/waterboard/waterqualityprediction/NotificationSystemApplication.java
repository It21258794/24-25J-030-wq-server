package com.waterboard.waterqualityprediction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.waterboard"}, exclude = {ErrorMvcAutoConfiguration.class})
public class NotificationSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationSystemApplication.class, args);
    }
}
