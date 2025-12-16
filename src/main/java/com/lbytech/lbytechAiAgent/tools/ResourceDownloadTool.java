package com.lbytech.lbytechAiAgent.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.lbytech.lbytechAiAgent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;

/**
 * 资源下载工具
 */
public class ResourceDownloadTool {

    /**
     * 下载资源
     * @param url 资源url
     * @param fileName 保存名字
     * @return 下载结果
     */
    @Tool(description = "Download resource from the given url with a specified name")
    public String downloadResource(@ToolParam(description = "Resource url to download") String url,
                                   @ToolParam(description = "The file name of the downloaded resource") String fileName) {
        String fileDir = FileConstant.FILE_SAVE_PATH + "download";
        String filePath = fileDir + "/" + fileName;
        try {
            // 确保目录存在
            FileUtil.mkdir(fileDir);

            // 下载资源
            HttpUtil.downloadFile(url, new File(filePath));
            return "Resource downloaded successfully to " + filePath;
        } catch (Exception e) {
            return "Error downloading resource : " + e.getMessage();
        }
    }

}
