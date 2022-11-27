package com.sheep.cloud.os.context;

import com.sheep.cloud.common.FileLoadEnum;
import com.sheep.cloud.os.OsStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author Zhang Jinming
 * @date 24/11/2022 下午4:51
 */
@Service
public class OsStrategyContext {
    @Autowired
    private Map<String, OsStrategy> uploadStrategyMap;

    public String executeUploadStrategy(MultipartFile file, String path, String mode) {
        return uploadStrategyMap.get(FileLoadEnum.getStrategy(mode)).uploadFile(file, path);
    }

    public void executeDeleteStrategy(String path, String mode) {
        uploadStrategyMap.get(FileLoadEnum.getStrategy(mode)).deleteFile(path);
    }
}
