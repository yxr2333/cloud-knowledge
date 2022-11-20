package com.sheep.cloud.dto.request.knowledge;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.request
 * @datetime 2022/8/13 星期六
 */
@Data
@NoArgsConstructor
public class IUsersAddScoreVO {

    @NotNull(message = "添加的积分不能为空")
    @Min(value = 1, message = "添加的积分必须大于0")
    private Integer score;

    @NotNull(message = "用户编号不能为空")
    private Integer id;
}
