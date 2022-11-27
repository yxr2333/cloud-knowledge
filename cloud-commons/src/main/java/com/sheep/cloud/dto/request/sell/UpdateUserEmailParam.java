package com.sheep.cloud.dto.request.sell;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Zhang Jinming
 * @date 25/11/2022 下午5:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserEmailParam {
    @NotNull(message = "用户编号不能为空")
    private Integer id;

    @NotNull(message = "未选择填写邮箱")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",
            message = "邮箱格式不正确")
    private String email;

    @NotNull(message = "未填写验证码")
    private String code;

    @Length(min = 10, max = 10, message = "请求标识符长度必须为10")
    private String requestKey;
}
