package com.atguigu.guli.service.oss.service;

import java.io.InputStream;

/**
 * @Description:
 * @author: zxl
 * @Data:2020/9/1
 */
public interface FileService {
    /**
     * 阿里云文件上传
     * @param inputStream 输入流
     * @param module 文件夹名称
     * @param originalFilename 原始文件名
     * @return 文件的url地址
     */
    String upload(InputStream inputStream, String module, String originalFilename);
    void removeFile(String url);
}
