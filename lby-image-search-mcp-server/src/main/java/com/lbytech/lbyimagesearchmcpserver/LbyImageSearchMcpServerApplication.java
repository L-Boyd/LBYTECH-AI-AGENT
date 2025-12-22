package com.lbytech.lbyimagesearchmcpserver;

import com.lbytech.lbyimagesearchmcpserver.tools.ImageSearchTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "file:./environmentVariables.env", ignoreResourceNotFound = false)
public class LbyImageSearchMcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LbyImageSearchMcpServerApplication.class, args);
    }

    /**
     * 系统启动时，自动注册图片搜索工具
     * @param imageSearchTool
     * @return
     */
    @Bean
    public ToolCallbackProvider imageSearchTools(ImageSearchTool imageSearchTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(imageSearchTool)
                .build();
    }

}
