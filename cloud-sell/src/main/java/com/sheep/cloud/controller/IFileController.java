package com.sheep.cloud.controller;

import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.IRemoteUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
@Api(tags = "文件上传")
public class IFileController {

    @Autowired
    private IRemoteUserService remoteFileService;

    @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "MultipartFile")
    @ApiOperation(value = "上传文件（新）", notes = "上传文件（新）")
    @PostMapping(value = "/upload/new", consumes = "multipart/form-data", produces = "application/json")
    public ApiResult<?> uploadImageNew(@RequestPart(name = "file") MultipartFile file) throws IOException {
        System.out.println("有人上传文件了");
        return remoteFileService.uploadImgNew(file);
    }

    @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "MultipartFile")
    @ApiOperation(value = "上传文件（旧）", notes = "上传文件（旧）")
    @PostMapping(value = "/upload", consumes = "multipart/form-data", produces = "application/json")
    public ApiResult<?> uploadImage(@RequestPart(name = "file") MultipartFile file) throws IOException {
        System.out.println("有人上传文件了");
        return remoteFileService.uploadImg(file);
    }


}
