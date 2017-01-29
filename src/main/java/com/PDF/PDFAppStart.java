package com.PDF;

import com.PDF.model.StorageProperties;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.PDF.service.StorageService;

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

        System.out.println("haha, and it begins");

        Document document  = new Document();

        try {
            PdfWriter.getInstance(document,
                    new FileOutputStream("Image.pdf"));
            document.open();

            Image image1 = Image.getInstance("me.jpg");
            document.add(image1);

            image1.scalePercent(300f);
            image1.isScaleToFitLineWhenOverflow();
            document.add(image1);

            document.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

//    @Bean
//    CommandLineRunner init(StorageService storageService) {
//        return (args) -> {
//            storageService.deleteAll();
//            storageService.init();
//        };
//    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PDFAppStart.class);
    }


}
