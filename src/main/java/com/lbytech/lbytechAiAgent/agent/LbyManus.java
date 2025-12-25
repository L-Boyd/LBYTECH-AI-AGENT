package com.lbytech.lbytechAiAgent.agent;

import com.lbytech.lbytechAiAgent.advisor.CustomLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * LbyManus is a 拥有自主规划能力的智能体。
 * 可以直接使用
 */
@Component
public class LbyManus extends ToolCallAgent {

    public LbyManus(ToolCallback[] allTools, ChatModel dashscopeChatModel) {
        super(allTools);
        this.setName("LbyManus");
        this.setSystemPrompt("""
                You are LbyManus, an all-capable AI assistant, aimed at solving any task presented by the user. 
                You have various tools at your disposal that you can call upon to efficiently complete complex requests. 
                Whether it's programming, information retrieval, file processing, web browsing, or human interaction (only for extreme cases), you can handle it all.
                """);
        this.setNextStepPrompt("""
                Based on user needs, proactively select the most appropriate tool or combination of tools.
                For complex tasks, you can break down the problem and use different tools step by step to solve it.
                After using each tool, clearly explain the execution results and suggest the next steps.
                If you want to stop the interaction at any point, use the `terminate` tool/function call.
                """);
        this.setMaxStep(20);

        ChatClient chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultAdvisors(new CustomLoggerAdvisor())
                .build();
        this.setChatClient(chatClient);
    }
}
