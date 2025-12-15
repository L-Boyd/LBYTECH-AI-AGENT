package com.lbytech.lbytechAiAgent.tools;

import org.junit.jupiter.api.Test;

class FileOperationToolTest {

    private FileOperationTool tool = new FileOperationTool();

    @Test
    void readFile() {
        System.out.println(tool.readFile("test.txt"));
    }

    @Test
    void writeFile() {
        System.out.println(tool.writeFile("test.txt", "test 测试文件写入"));
        System.out.println(tool.readFile("test.txt"));
    }
}