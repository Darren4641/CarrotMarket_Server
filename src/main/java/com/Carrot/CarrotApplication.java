package com.Carrot;

import com.File.FileUploadProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

@EnableConfigurationProperties({FileUploadProperties.class})
@SpringBootApplication
public class CarrotApplication {



    public static void main(String[] args) {
        SpringApplication.run(CarrotApplication.class, args);
    }
}
