package com.sheep.cloud.dto.request.sell;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Zhang Jinming
 * @date 25/11/2022 下午4:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfoParam {
    @NotNull(message = "用户编号不能为空")
    private Integer id;

    @NotNull(message = "未选择填写用户名")
    private String username;

    private String description;

    private String avatar;
}
