package com.sheep.cloud.os.impl;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Zhang Jinming
 * @date 24/11/2022 下午5:20
 */
@Service
public class CosClientServiceImpl extends AbstractOsStrategyImpl{
    @Override
    public void delete(String url) {

    }

    @Override
    public Boolean exists(String filePath) {
        return null;
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream, String contentType) throws IOException {

    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return null;
    }
}
