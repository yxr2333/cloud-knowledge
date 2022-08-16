package com.sheep.cloud.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.response
 * @datetime 2022/8/16 星期二
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IWishBaseInfoDTO {

    private Integer id;

    private String content;

    private IUsersBaseInfoDTO user;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    private Boolean isFinished;

    private List<IBaseLabelInfoDTO> labels;
}
