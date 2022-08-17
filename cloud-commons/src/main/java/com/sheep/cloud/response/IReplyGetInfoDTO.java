package com.sheep.cloud.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sheep.cloud.entity.IRepliesEntity;
import com.sheep.cloud.entity.IUsersEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author ALEX
 * @since 2022/8/15 16:29
 */
@Data
@NoArgsConstructor
public class IReplyGetInfoDTO implements Serializable {
    private int id;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp publishTime;
    private IReplySecondInfoDTO lastReplyId;
    private ICommentGetUserInfoDTO replyUser;
}
