package com.lbytech.lbytechAiAgent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "file:./environmentVariables.env", ignoreResourceNotFound = true)
public class LbytechAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(LbytechAiAgentApplication.class, args);
    }

}
