package com.Carrot;

import com.File.FileUploadProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({FileUploadProperties.class})
@SpringBootApplication
public class CarrotApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarrotApplication.class, args);
    }
}
