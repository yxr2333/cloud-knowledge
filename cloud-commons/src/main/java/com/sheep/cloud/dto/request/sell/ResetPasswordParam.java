package com.sheep.cloud.dto.request.sell;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.dto.request
 * @datetime 2022/9/20 星期二
 */
@Data
public class ResetPasswordParam {

    @NotNull(message = "用户名不能为空")
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotNull(message = "密码不能为空")
    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotNull(message = "新密码不能为空")
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;

    @Length(min = 5, max = 5, message = "验证码长度必须为5")
    private String code;


    @Length(min = 10, max = 10, message = "请求标识符长度必须为10")
    private String requestKey;
}
