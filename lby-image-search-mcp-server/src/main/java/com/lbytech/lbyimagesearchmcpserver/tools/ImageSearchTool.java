package com.lbytech.lbyimagesearchmcpserver.tools;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImageSearchTool {

    private static final String API_URL = "https://api.pexels.com/v1/search";

    @Value("${lbytech.pexels.apiKey}")
    private String API_KEY;

    @Tool(description = "Search image by query")
    public String searchImage(@ToolParam(description = "The image query keyword to search") String query) {
        try {
            return String.join(",\n", search(query));
        } catch (Exception e) {
            return "Error search image: " + e.getMessage();
        }
    }

    private List<String> search(String query) {
        int perPage = 10;        // 每页数量
        int page = 1;            // 页码

        // 构建请求
        HttpResponse response = HttpRequest.get(API_URL)
                .header(Header.AUTHORIZATION, API_KEY)
                .form("query", query)
                .form("per_page", perPage)
                .form("page", page)
                .timeout(10000) // 超时 10 秒
                .execute();

        if (response.isOk()) {
            String body = response.body();
            JSONObject json = JSONUtil.parseObj(body);

            log.info("总结果数: " + json.getInt("total_results"));
            log.info("当前页: " + json.getInt("page"));
            log.info("每页数量: " + json.getInt("per_page"));

            return json.getJSONArray("photos")
                    .stream()
                    .map(photoObj -> (JSONObject) photoObj)
                    .map(photoObject -> photoObject.getJSONObject("src"))
                    .map(photo -> photo.getStr("medium"))
                    .filter(StrUtil::isNotBlank)
                    .collect(Collectors.toList());
        } else {
            log.error("响应内容: " + response.body());
            throw new RuntimeException("请求失败，状态码: " + response.getStatus());
        }
    }

}
