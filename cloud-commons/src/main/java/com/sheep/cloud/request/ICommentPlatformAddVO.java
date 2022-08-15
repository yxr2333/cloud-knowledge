package com.sheep.cloud.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ALEX
 * @since 2022/8/15 13:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ICommentPlatformAddVO {
    private String content;
    private Integer publishUser;
}
