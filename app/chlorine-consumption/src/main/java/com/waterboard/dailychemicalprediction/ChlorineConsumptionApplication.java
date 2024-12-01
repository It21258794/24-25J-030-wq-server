package com.waterboard.dailychemicalprediction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.waterboard"},
    exclude = {ErrorMvcAutoConfiguration.class, DataSourceAutoConfiguration.class})
// @EnableJpaRepositories(basePackages = "com.waterboard.waterqualityprediction.repo")  // adjust the package name
// @EntityScan(basePackages = "com.waterboard.waterqualityprediction.model")  // adjust the package name
public class ChlorineConsumptionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChlorineConsumptionApplication.class, args);
    }
}
