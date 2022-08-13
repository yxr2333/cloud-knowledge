package com.sheep.cloud.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.request
 * @datetime 2022/8/12 星期五
 */
@Data
@NoArgsConstructor
public class IUsersResetPasswordVO implements Serializable {

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

}
