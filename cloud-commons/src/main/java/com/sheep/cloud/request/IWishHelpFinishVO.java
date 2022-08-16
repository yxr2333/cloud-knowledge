package com.sheep.cloud.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
public class IWishHelpFinishVO {

    @NotNull(message = "用户名不能为空")
    @NotEmpty(message = "用户名不能为空")
    private String username;
    
    private String avatar;

    private String content;

    @NotNull(message = "资源id不能为空")
    private Integer resourceId;

    @NotNull(message = "心愿id不能为空")
    private Integer wishId;
}
