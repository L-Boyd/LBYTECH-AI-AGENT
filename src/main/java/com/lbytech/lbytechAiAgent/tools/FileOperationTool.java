package com.lbytech.lbytechAiAgent.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import com.lbytech.lbytechAiAgent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 文件操作工具类（提供文件读写功能）
 */
public class FileOperationTool {

    private final String FILE_DIR = FileConstant.FILE_SAVE_PATH;

    @Tool(description = "Read content from a file")
    public String readFile(@ToolParam(description = "The name of the file to read") String fileName) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "Error reading file " + e.getMessage();
        }
    }

    @Tool(description = "Write content to a file")
    public String writeFile(@ToolParam(description = "The name of the file to write") String fileName,
                          @ToolParam(description = "The content to write to the file") String content) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            // 确保目录存在
            FileUtil.mkdir(FILE_DIR);
            FileUtil.writeUtf8String(content, filePath);
            return "File written successfully to:" + filePath;
        } catch (Exception e) {
            return "Error writing file " + e.getMessage();
        }
    }

}
