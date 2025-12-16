package com.lbytech.lbytechAiAgent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerminalOperationToolTest {

    private TerminalOperationTool terminalOperationTool = new TerminalOperationTool();

    @Test
    void executeTerminalCommand() {
        String command = "dir";
        String result = terminalOperationTool.executeTerminalCommand(command);
        Assertions.assertNotNull(result);
        System.out.println(result);
    }
}