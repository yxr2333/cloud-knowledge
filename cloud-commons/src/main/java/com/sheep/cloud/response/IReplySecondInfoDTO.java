package com.sheep.cloud.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ALEX
 * @since 2022/8/15 16:37
 */
@Data
@NoArgsConstructor
public class IReplySecondInfoDTO implements Serializable{
    private int id;
    private ICommentGetUserInfoDTO replyUser;
}
