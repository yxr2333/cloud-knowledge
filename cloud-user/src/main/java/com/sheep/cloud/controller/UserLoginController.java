package com.sheep.cloud.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.sheep.cloud.response.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.controller
 * @datetime 2022/8/11 星期四
 */
@RestController
@RequestMapping("/user")
public class UserLoginController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/login")
    public ApiResult login(String username, String password) {
        if ("yxr".equals(username) && "123456".equals(password)) {
            StpUtil.login("1");
            HashMap<String, Object> token = new HashMap<>();
            token.put("token", StpUtil.getTokenValue());
            token.put("header", StpUtil.getTokenName());
            return ApiResult.success(token);
        } else {
            return ApiResult.error("用户名或密码错误");
        }
    }
}
