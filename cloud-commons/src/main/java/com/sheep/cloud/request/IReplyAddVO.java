package com.sheep.cloud.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ALEX
 * @since 2022/8/14 8:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IReplyAddVO {
<<<<<<< HEAD
    String content;
    Integer commentId;
    Integer lastReplyId;
    Integer replyUser;
=======
    private String content;
    private Integer commentId;
    private Integer lastReplyId;
    private Integer replyUser;
>>>>>>> 116a605 (新增：完成评论角模块)
}
