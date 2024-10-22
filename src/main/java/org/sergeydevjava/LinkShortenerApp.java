package org.sergeydevjava;


import com.sergeydevjava.LoggingStarterAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LinkShortenerApp {

    public static void main(String[] args) {
        LoggingStarterAutoConfiguration.println("Hello world");
        SpringApplication.run(LinkShortenerApp.class, args);
    }
}