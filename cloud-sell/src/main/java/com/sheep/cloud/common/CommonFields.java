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

    public static final String MAIN_WEB_USER_APPID_URL = "http://localhost:8009/oauth2/token?grant_type=authorization_code&client_id=1&client_secret=M2U5ZklRTVlDVzJHQkxU";

}
