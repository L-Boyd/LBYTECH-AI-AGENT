package com.lbytech.lbytechAiAgent.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class WebSearchProperties {

    private String searchApiUrl = "https://www.searchapi.io/api/v1/search";

    @Value("${lbytech.searchapi.api-key}")
    private String apiKey;

}