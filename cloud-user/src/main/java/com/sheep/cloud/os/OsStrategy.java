package com.sheep.cloud.os;

import org.springframework.web.multipart.MultipartFile;

/**
 * 上传策略
 *
 * @author Elm Forest
 */
public interface OsStrategy {

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 上传路径
     * @return {@link String} 文件地址
     */
    String uploadFile(MultipartFile file, String path);

    /**
     * 删除文件
     *
     * @param url 路径
     */
    void deleteFile(String url);
}
