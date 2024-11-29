package org.sergeydevjava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class LinkShortenerConfig {

    @Bean
    public String notFoundPage() throws IOException {
        try (InputStream is = new ClassPathResource("templates/404.html").getInputStream()) {
            byte[] binaryData = FileCopyUtils.copyToByteArray(is);
            return new String(binaryData, StandardCharsets.UTF_8);
        }
    }
}
