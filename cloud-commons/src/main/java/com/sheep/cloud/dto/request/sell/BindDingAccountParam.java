package com.sheep.cloud.dto.request.sell;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.dto.request
 * @datetime 2022/9/20 星期二
 */

@Data
public class BindDingAccountParam {

    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "密码不能为空")
    private String password;

    @NotNull(message = "钉钉授权码不能为空")
    private String openid;

    @NotNull(message = "邮箱不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式不正确")
    private String email;

    @NotNull(message = "个人介绍不能为空")
    private String description;

}
