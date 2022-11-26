package com.sheep.cloud.controller;

import com.sheep.cloud.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zhang Jinming
 * @date 26/11/2022 下午3:26
 */
@RestController
@RequestMapping("/os")
public class OsController {
    @Autowired
    private UploadService uploadService;

    @PostMapping
    public String uploadFile(MultipartFile file, String path, String mode) {
        return uploadService.uploadFile(file, path, mode);
    }

    @DeleteMapping
    public String deleteFile(String path, String mode) {
        uploadService.deleteFile(path, mode);
        return "hello world";
    }
}
