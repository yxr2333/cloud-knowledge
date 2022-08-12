package com.sheep.cloud.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.request
 * @datetime 2022/8/12 星期五
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IUsersRegisterVO implements Serializable {

    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "密码不能为空")
    private String password;

    @NotNull(message = "邮箱不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式不正确")
    private String email;

    @NotNull(message = "个人介绍不能为空")
    private String description;
}
