package com.sheep.cloud.common;

import lombok.Data;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.common
 * @datetime 2022/9/21 星期三
 */
@Data
public class CommonFields {

    public static final String VERIFY_CODE_NAME = "reset_pwd";

    public static final String USER_INFO_CODE_URL = "https://oapi.dingtalk.com/sns/getuserinfo_bycode";

}
