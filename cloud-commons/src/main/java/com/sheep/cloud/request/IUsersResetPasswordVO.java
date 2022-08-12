package com.sheep.cloud.request;

import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String username;
    private String password;
    private String newPassword;
    private String code;

}
