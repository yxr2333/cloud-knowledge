package com.sheep.cloud.dto.response.knowledge;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author ALEX
 * @since 2022/8/19 14:25
 */
@Data
@NoArgsConstructor
public class ICommentPlatformGetUserInfoDTO {
    private Integer id;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;
    private ICommentGetUserInfoDTO publishUser;
}
