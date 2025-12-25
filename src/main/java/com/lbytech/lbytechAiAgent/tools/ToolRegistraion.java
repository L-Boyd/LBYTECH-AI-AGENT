package com.lbytech.lbytechAiAgent.tools;

import jakarta.annotation.Resource;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工具注册类
 */
@Configuration
public class ToolRegistraion {

    @Autowired
    private WebSearchTool webSearchTool;

    /**
     * 注册所有工具
     *
     * 用了工厂模式：一个allTool，包含所有工具。工厂模式核心思想：集中创建对象并隐藏创建细节
     * 注册模式：该类作为一个中央注册点，集中管理和注册所有可用的工具，使它们能够被系统而其它部分统一访问
     * 适配器模式：ToolCallbacks.from方法可以看作是一种适配器，他将各种不同的工具类转换为统一的TollCallback数组，使系统能够以一致的方式处理它们
     * @return
     */
    @Bean
    public ToolCallback[] allTools() {
        return ToolCallbacks.from(
                new FileOperationTool(),
                webSearchTool,
                new WebScrapingTool(),
                new ResourceDownloadTool(),
                new TerminalOperationTool(),
                new PDFGenerationTool(),
                new TerminateTool()
        );
    }

}
