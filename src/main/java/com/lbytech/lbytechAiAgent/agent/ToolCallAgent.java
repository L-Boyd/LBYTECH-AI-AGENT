package com.lbytech.lbytechAiAgent.agent;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.lbytech.lbytechAiAgent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理工具调用的基础代理类，
 * 具体实现了think和react方法，
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ToolCallAgent extends ReActAgent{

    // 可用的工具
    private final ToolCallback[] availableTools;
    // 工具提供者(方便用MCP）
    private ToolCallbackProvider toolCallbackProvider;

    // 工具调用信息的响应结果（要调用哪些工具）
    private ChatResponse toolCallChatResponse;

    // 工具调用管理者
    private final ToolCallingManager toolCallingManager;

    // 禁用spring ai内置的工具调用机制，自己维护选项和消息上下文
    private final ChatOptions chatOptions;

    public ToolCallAgent(ToolCallback[] availableTools) {
        super();
        this.availableTools = availableTools;
        this.toolCallingManager = ToolCallingManager.builder().build();
        this.chatOptions = DashScopeChatOptions.builder()   // 百炼和spring ai原生配置方式有点不兼容
                .withProxyToolCalls(true)   // 禁用spring ai内置的工具调用机制，自己维护选项和消息上下文
                .build();
    }

    public ToolCallAgent(ToolCallback[] availableTools, ToolCallbackProvider toolCallbackProvider) {
        super();
        this.availableTools = availableTools;
        this.toolCallbackProvider = toolCallbackProvider;
        this.toolCallingManager = ToolCallingManager.builder().build();
        this.chatOptions = DashScopeChatOptions.builder()   // 百炼和spring ai原生配置方式有点不兼容
                .withProxyToolCalls(true)   // 禁用spring ai内置的工具调用机制，自己维护选项和消息上下文
                .build();
    }

    @Override
    public boolean think() {
        String nextStepPrompt = this.getNextStepPrompt();
        List<Message> messageList = this.getMessageList();
        ChatClient chatClient = this.getChatClient();
        String name = this.getName();

        try {
            // 校验提示词、拼接用户提示词
            if (StrUtil.isNotBlank(nextStepPrompt)) {
                UserMessage userMessage = new UserMessage(nextStepPrompt);
                messageList.add(userMessage);
            }

            Prompt prompt = new Prompt(messageList, this.chatOptions);
            // 调用模型
            ChatResponse chatResponse = chatClient.prompt(prompt)
                    .system(this.getSystemPrompt())
                    .tools(this.availableTools)
                    .tools(this.toolCallbackProvider)
                    .call()
                    .chatResponse();
            this.toolCallChatResponse = chatResponse;

            // 助手消息
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            // 要调用的工具列表
            List<AssistantMessage.ToolCall> toolCallList = assistantMessage.getToolCalls();

            // 输出提示信息
            log.info("{} 的思考：{}", name, assistantMessage.getText());
            log.info("{} 选择了{} 个工具", name, toolCallList.size());
            String toolCallInfo = toolCallList.stream()
                    .map(toolCall ->
                            String.format("工具名称：%s，参数：%s", toolCall.name(), toolCall.arguments())
                    )
                    .collect(Collectors.joining("\n"));
            log.info("{} 要调用的工具列表：{}", name, toolCallInfo);

            if (toolCallList.isEmpty()) {
                // 不调用工具时才需要手动记录助手消息
                messageList.add(assistantMessage);
                // 没有要调用的工具，直接返回
                return false;
            } else {
                // 需要调用工具时，无需记录助手消息，因为调用工具时会自动记录
                return true;
            }
        } catch (Exception e) {
            log.error("{} 思考时发生异常：{}", this.getName(), e.getMessage());
            messageList.add(new AssistantMessage("处理时遇到错误：" + e.getMessage()));
            return false;
        }

    }

    @Override
    public String act() {
        if (!this.toolCallChatResponse.hasToolCalls()) {
            return "没有工具需要调用";
        }

        // 调用工具
        Prompt prompt = new Prompt(this.getMessageList(), this.chatOptions);
        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, toolCallChatResponse);

        // 记录消息上下文，conversationHistory已经包含助手消息和工具调用返回的结果
        List<Message> newMessageList = toolExecutionResult.conversationHistory();
        this.setMessageList(newMessageList);

        ToolResponseMessage toolResponseMessage = (ToolResponseMessage) CollUtil.getLast(newMessageList);
        String results = toolResponseMessage.getResponses()
                .stream()
                .map(response -> "工具" + response.name() + "的结果是：" + response.responseData())
                .collect(Collectors.joining("\n"));
        log.info("工具调用结果：{}", results);

        // 判断是否调用了终止工具
        boolean terminateTollCalled = toolResponseMessage.getResponses()
                .stream()
                .anyMatch(response -> response.name().equals("terminate"));
        if (terminateTollCalled) {
            this.setState(AgentState.FINISHED);
        }

        return results;
    }
}
