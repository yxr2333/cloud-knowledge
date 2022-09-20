package com.sheep.cloud.dto.request;

import lombok.Data;

import java.io.Serializable;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.dto.request
 * @datetime 2022/9/20 星期二
 */
@Data
public class UserLoginParam implements Serializable {

    private String username;

    private String password;
}
