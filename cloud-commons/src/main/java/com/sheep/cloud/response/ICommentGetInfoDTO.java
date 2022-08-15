package com.sheep.cloud.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sheep.cloud.entity.IUsersEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ALEX
 * @since 2022/8/14 21:26
 */
@Data
@NoArgsConstructor
public class ICommentGetInfoDTO implements Serializable {
    private Integer id;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;
    private ICommentGetUserInfoDTO publishUser;
}
