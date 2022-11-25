package com.sheep.cloud.service.impl;

import com.sheep.cloud.common.FileLoadEnum;
import com.sheep.cloud.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author Zhang Jinming
 * @date 24/11/2022 下午4:51
 */
@Service
public class UploadStrategyContext {
    @Autowired
    private Map<String, UploadService> uploadStrategyMap;

    public String executeUploadStrategy(MultipartFile file, String path, String mode) {
        return uploadStrategyMap.get(FileLoadEnum.getStrategy(mode)).uploadFile(file, path, mode);
    }

    public String executeDeleteStrategy(String path, String mode) {
        return uploadStrategyMap.get(FileLoadEnum.getStrategy(mode)).deleteFile(path, mode);
    }
}
