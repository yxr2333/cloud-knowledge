package com.sheep.cloud.controller;

import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.IRemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/11/23 星期三
 * Happy Every Coding Time~
 */

@RequestMapping("/file")
@RestController
public class IFileController {

    @Autowired
    private IRemoteUserService remoteFileService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data", produces = "application/json")
    public ApiResult<?> uploadImage(@RequestPart(name = "file") MultipartFile file) throws IOException {
        System.out.println("有人上传文件了");
        return remoteFileService.uploadImg(file);

    }
}
