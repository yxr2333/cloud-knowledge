package com.sheep.cloud.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author ALEX
 * @since 2022/8/19 14:25
 */
@Data
@NoArgsConstructor
public class ICommentPlatformGetUserInfoDTO {
    private Integer id;
    private String content;
    private Timestamp publishTime;
    private ICommentGetUserInfoDTO publishUser;
}
