package com.lbytech.lbytechAiAgent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 终端操作工具
 */
public class TerminalOperationTool {

    /**
     * 执行终端命令
     * @param command 终端命令
     * @return 命令执行结果
     */
    @Tool(description = "Execute a terminal command")
    public String executeTerminalCommand(@ToolParam(description = "Terminal command to execute") String command) {
        StringBuilder output = new StringBuilder();
        try {
            // 执行命令
            //Process process = Runtime.getRuntime().exec(command);
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
            Process process = builder.start();

            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            // 执行失败
            if (exitCode != 0) {
                output.append("Command execution failed with exit code: ").append(exitCode);
            }
        } catch (Exception e) {
            output.append("Error executing terminal command: ").append(e.getMessage());
        }

        return output.toString();
    }


}
