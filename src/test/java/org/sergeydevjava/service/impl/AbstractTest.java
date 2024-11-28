package org.sergeydevjava.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sergeydevjava.property.LinkInfoProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.nio.file.Files;

@SpringBootTest
@AutoConfigureMockMvc
public class AbstractTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected LinkInfoProperty linkInfoProperty;


    public String convertFileToString(String path) throws IOException {
        return new String(Files.readAllBytes(new ClassPathResource(path).getFile().toPath()));
    }

    public String parseHeader(MvcResult mvcResult, String header) throws Exception {
        return mvcResult.getResponse().getHeader(header);
    }
}
