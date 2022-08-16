package com.sheep.cloud.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.request
 * @datetime 2022/8/16 星期二
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IWishPublishVO {

    @NotNull(message = "请填写心愿内容")
    @NotEmpty(message = "请填写心愿内容")
    private String content;

    @NotNull(message = "请选择发布人")
    private Integer publishUserId;

    @NotNull(message = "请选择心愿标签")
    private List<Integer> labelIds;
}
