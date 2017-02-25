package com.PDF;

import com.PDF.model.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * Created by martanase on 12/9/2016.
 */
//@Configuration
//@ComponentScan(basePackages = { "com.endava.weatherapp" })
//@ImportResource(value = { "classpath:beans.xml" })
@SpringBootApplication
@Configuration
@ComponentScan(basePackages = { "com.PDF" })
//@EnableAutoConfiguration
@EnableConfigurationProperties(StorageProperties.class)
public class PDFAppStart extends SpringBootServletInitializer{

    public static void main(String[] args) {

        SpringApplication.run(PDFAppStart.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PDFAppStart.class);
    }


}
