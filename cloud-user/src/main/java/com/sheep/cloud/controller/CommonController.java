package com.sheep.cloud.controller;

import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.controller
 * @datetime 2022/8/15 星期一
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Autowired
    private CommonService commonService;

    @GetMapping("/labelCategoryMenu")
    public ApiResult getAllLabelCategoryMenu() {
        return commonService.getAllLabelCategoryMenu();
    }

    @GetMapping("/getAllLabels")
    public ApiResult getAllLabels() {
        return commonService.getAllLabels();
    }

    @PostMapping("/upload")
    public ApiResult uploadImg(@RequestParam("file") MultipartFile file) throws IOException {
        return commonService.uploadFile(file);
    }
}
