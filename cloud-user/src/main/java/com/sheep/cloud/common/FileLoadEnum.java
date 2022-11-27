package com.sheep.cloud.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zhang Jinming
 * @date 23/11/2022 下午10:25
 */
@Getter
@AllArgsConstructor
public enum FileLoadEnum {
    /**
     * oss
     */
    OSS("oss", "ossClientServiceImpl"),
    /**
     * cos
     */
    COS("cos", "cosClientServiceImpl");

    /**
     * 模式
     */
    private final String mode;

    /**
     * 策略
     */
    private final String strategy;

    public static String getStrategy(String mode) {
        for (FileLoadEnum fileEnum:FileLoadEnum.values()) {
            if (fileEnum.getMode().equals(mode)) {
                return fileEnum.getStrategy();
            }
        }
        return null;
    }
}
