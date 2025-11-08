package com.lbytech.lbytechAiAgent.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
@Getter
public class DashScopeConfig {

    @Value("${lbytech.ali.api-key}")
    private String apiKey;

}