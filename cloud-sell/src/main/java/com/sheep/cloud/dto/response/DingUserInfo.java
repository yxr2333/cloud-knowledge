package com.sheep.cloud.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.dto.response
 * @datetime 2022/9/20 星期二
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DingUserInfo {

    private String nickname;
    private String unionId;
    private String openId;
}
