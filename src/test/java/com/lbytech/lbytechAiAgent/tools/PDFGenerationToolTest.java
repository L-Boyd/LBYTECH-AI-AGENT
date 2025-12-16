package com.lbytech.lbytechAiAgent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDFGenerationToolTest {

    private PDFGenerationTool tool = new PDFGenerationTool();

    @Test
    void generatePDF() {
        String content = "This is a test";
        String fileName = "test.pdf";
        String result = tool.generatePDF(fileName, content);
        System.out.println(result);
    }
}