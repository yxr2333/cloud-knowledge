package com.sheep.cloud.request;

import lombok.*;

/**
 * @author ALEX
 * @since 2022/8/14 7:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ICommentAddVO {
    String content;
    Integer publishUser;
    Integer resourceId;
}
