package com.sheep.cloud.os.impl;

import com.sheep.cloud.os.OsStrategy;
import com.sheep.cloud.store.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Elm Forest
 */
@Service
public abstract class AbstractOsStrategyImpl implements OsStrategy {
    /**
     * 上传文件
     *
     * @param file MultipartFile格式
     * @param path 上传路径
     *             相对于根目录的路径，路径前不能加/，路径后必须加/
     *             eg: user/elm45678122/
     * @return {@link String} 文件md5值
     */

    @Override
    public String uploadFile(MultipartFile file, String path) {
        try {
            // 获取文件md5值
            String md5 = FileUtils.getMd5(file.getInputStream());
            // 获取文件扩展名
            String extName = FileUtils.getExtName(file.getOriginalFilename());
            // 重新生成文件名
            String fileName = md5 + extName;
            // 判断文件是否已存在
            if (!exists(path + fileName)) {
                // 不存在则继续上传
                upload(path, fileName, file.getInputStream(), file.getContentType());
            }
            // 返回文件访问路径
            return getFileAccessUrl(path + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败");
        }
    }

    @Override
    public void deleteFile(String path) {
        try {
            if (exists(path)) {
                throw new RuntimeException("路径:" + path + "不存在");
            }
            delete(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void delete(String url);

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@link Boolean}
     */
    public abstract Boolean exists(String filePath);

    /**
     * 上传
     *
     * @param path        路径
     * @param fileName    文件名
     * @param inputStream 输入流
     * @throws IOException io异常
     */
    public abstract void upload(String path, String fileName, InputStream inputStream, String contentType) throws IOException;

    /**
     * 获取文件访问url
     *
     * @param filePath 文件路径
     * @return {@link String}
     */
    public abstract String getFileAccessUrl(String filePath);

}
