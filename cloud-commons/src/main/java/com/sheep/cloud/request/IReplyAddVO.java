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
    String content;
    Integer commentId;
    Integer lastReplyId;
    Integer replyUser;
}
