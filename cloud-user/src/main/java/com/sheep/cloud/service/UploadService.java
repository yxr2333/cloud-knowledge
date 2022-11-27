package com.sheep.cloud.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zhang Jinming
 * @date 23/11/2022 下午8:38
 */
public interface UploadService {

    String uploadFile(MultipartFile file, String path, String mode);


    void deleteFile(String url, String mode);
}
