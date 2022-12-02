package com.sheep.cloud.controller;

import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    public ApiResult<?> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam String path,
            @RequestParam String mode) {
        String url = uploadService.uploadFile(file, path, mode);
        return new ApiResult<>().success(null, url);
    }

    @PostMapping("/default")
    public ApiResult<?> uploadFileDefault(@RequestParam("file") MultipartFile file) {
        String url = uploadService.uploadFile(file, "test", "oss");
        return new ApiResult<>().success(null, url);
    }

    @DeleteMapping
    public String deleteFile(String path, String mode) {
        uploadService.deleteFile(path, mode);
        return "hello world";
    }
}
