package com.lbytech.lbytechAiAgent.agent;

import cn.hutool.core.util.StrUtil;
import com.lbytech.lbytechAiAgent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象基础代理类，用于管理代理状态和执行，
 * 提供状态转换、内存管理和基于步骤的执行循环的基础功能，
 * 子类必须实现step方法
 */
@Data
@Slf4j
public abstract class BaseAgent {

    /**
     * 代理名称，核心属性
     */
    private String name;

    /**
     * 提示词
     */
    private String systemPrompt;
    private String nextStepPrompt;

    /**
     * 代理状态，默认空闲状态
     */
    private AgentState state = AgentState.IDLE;

    /**
     * 执行步骤控制
     */
    private int currentStep = 0;
    private int maxStep = 10;

    /**
     * AI客户端
     */
    private ChatClient chatClient;

    /**
     * 消息列表，用于存储交互过程中的消息
     */
    private List<Message> messageList = new ArrayList<>();

    /**
     * 执行代理，根据用户提示生成响应
     *
     * @param userPrompt 用户输入的提示
     * @return 代理生成的响应
     */
    public String run(String userPrompt) {
        // 基础校验
        if (state != AgentState.IDLE) {
            throw new RuntimeException("Agent is not idle, current state: " + state);
        }
        if (StrUtil.isBlank(userPrompt)) {
            throw new RuntimeException("Agent userPrompt is empty");
        }

        try {
            // 执行
            this.state = AgentState.RUNNING;
            // 记录消息上下文
            messageList.add(new UserMessage(userPrompt));
            // 结果列表
            List<String> results = new ArrayList<>();
            for (int i = 0; i < maxStep && this.state != AgentState.FINISHED; i++) {
                currentStep = i + 1;
                log.info("Current step: {}/{}", currentStep, maxStep);
                // 执行一步
                String result = this.step();
                results.add("Step " + currentStep + ": " + result);
                results.add(result);
            }

            if (currentStep >= maxStep) {
                this.state = AgentState.FINISHED;
                results.add("Terminated: reached max step (" + maxStep + ")");
            }

            return String.join("\n", results);
        } catch (Exception e) {
            state = AgentState.ERROR;
            return "执行错误:" + e.getMessage();
        } finally {
            // 清理资源
            this.cleanup();
        }
    }

    /**
     * 执行代理的一步，子类必须实现
     *
     * @return 代理执行的结果
     */
    public abstract String step();

    /**
     * 清理资源，子类可以重写此方法来清理资源
     */
    protected void cleanup() {

    }

}
