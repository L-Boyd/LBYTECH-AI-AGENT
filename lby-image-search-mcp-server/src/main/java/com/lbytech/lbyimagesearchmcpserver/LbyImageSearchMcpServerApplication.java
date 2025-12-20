package com.lbytech.lbyimagesearchmcpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "file:../environmentVariables.env", ignoreResourceNotFound = false)
public class LbyImageSearchMcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LbyImageSearchMcpServerApplication.class, args);
    }

}
