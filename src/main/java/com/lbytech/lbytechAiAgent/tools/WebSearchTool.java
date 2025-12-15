package com.lbytech.lbytechAiAgent.tools;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lbytech.lbytechAiAgent.properties.WebSearchProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 网络搜索工具(利用https://www.searchapi.io)
 */
@Component
public class WebSearchTool {

    @Autowired
    private WebSearchProperties webSearchProperties;

    private String searchApiUrl;

    private String apiKey;

    @PostConstruct
    public void init() {
        this.searchApiUrl = webSearchProperties.getSearchApiUrl();
        this.apiKey = webSearchProperties.getApiKey();
    }

    @Tool(description = "Search for information from Baidu Search Engine")
    public String searchWeb(@ToolParam(description = "Search query keyword") String query) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("q", query);
        paramMap.put("api_key", apiKey);
        paramMap.put("engine", "baidu");

        try {
            String response = HttpUtil.get(searchApiUrl, paramMap);

            // 取出结果的前三条
            JSONObject jsonObject = JSONUtil.parseObj(response);
            JSONArray organicResults = jsonObject.getJSONArray("organic_results");
            List<Object> objects = organicResults.subList(0, 3);

            String result = objects.stream()
                    .map(obj -> {
                        JSONObject tempJsonObject = (JSONObject) obj;
                        return tempJsonObject.toString();
                    })
                    .collect(Collectors.joining(","));
            return result;
        } catch (Exception e) {
            return "Error searching web: " + e.getMessage();
        }
    }

}
