package com.sheep.cloud.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ALEX
 * @since 2022/8/15 16:20
 */
@Data
@NoArgsConstructor
public class ICommentGetUserInfoDTO implements Serializable {
    private int uid;
    private String username;
    private String avatar;
}
