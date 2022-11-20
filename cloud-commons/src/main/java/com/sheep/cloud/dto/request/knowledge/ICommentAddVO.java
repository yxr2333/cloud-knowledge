package com.sheep.cloud.dto.request.knowledge;

import lombok.*;

/**
 * @author ALEX
 * @since 2022/8/14 7:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ICommentAddVO {
    private String content;
    private Integer publishUser;
    private Integer resourceId;
}
