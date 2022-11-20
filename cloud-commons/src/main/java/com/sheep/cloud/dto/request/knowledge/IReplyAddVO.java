package com.sheep.cloud.dto.request.knowledge;

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
    private String content;
    private Integer commentId;
    private Integer lastReplyId;
    private Integer replyUser;
}
