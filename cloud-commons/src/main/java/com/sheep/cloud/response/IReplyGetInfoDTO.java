package com.sheep.cloud.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    private LocalDateTime publishTime;
    private IReplySecondInfoDTO lastReplyId;
    private ICommentGetUserInfoDTO replyUser;
}
