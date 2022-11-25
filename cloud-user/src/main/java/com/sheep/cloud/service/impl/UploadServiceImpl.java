package com.sheep.cloud.service.impl;

import com.sheep.cloud.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zhang Jinming
 * @date 23/11/2022 下午8:38
 */
@Service
@Slf4j
public class UploadServiceImpl implements UploadService {
    private final UploadStrategyContext uploadStrategyContext;

    @Autowired
    public UploadServiceImpl(UploadStrategyContext uploadStrategyContext) {
        this.uploadStrategyContext = uploadStrategyContext;
    }

    @Override
    public String uploadFile(MultipartFile file, String path, String mode) {
        return uploadStrategyContext.executeUploadStrategy(file, path, mode);
    }

    @Override
    public String deleteFile(String url, String mode) {
        return uploadStrategyContext.executeDeleteStrategy(url, mode);
    }
}
