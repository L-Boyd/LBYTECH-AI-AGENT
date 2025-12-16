package com.lbytech.lbytechAiAgent.tools;

import cn.hutool.core.io.FileUtil;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.lbytech.lbytechAiAgent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * PDF生成工具
 */
public class PDFGenerationTool {

    /**
     * 生成PDF文件
     *
     * @param content  PDF内容
     * @param fileName 文件名
     */
    @Tool(description = "Generate a PDF file with given content")
    public String generatePDF(@ToolParam(description = "Name of the PDF file") String fileName,
                            @ToolParam(description = "Content of the PDF file") String content) {
        String fileDir = FileConstant.FILE_SAVE_PATH + "/pdf";
        String filePath = fileDir + "/" + fileName;

        try {
            // 创建目录
            FileUtil.mkdir(fileDir);

            // 使用try-with-resources自动关闭PdfWriter
            try(PdfWriter pdfWriter = new PdfWriter(filePath)) {
                PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                Document document = new Document(pdfDocument);

                // 使用内置中文字体
                PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
                document.setFont(font);

                // 创建段落
                document.add(new Paragraph(content));
                document.close();
            }

            return "PDF generated successfully to: " + filePath;
        } catch (Exception e) {
            return "Error generating PDF: " + e.getMessage();
        }
    }


}
