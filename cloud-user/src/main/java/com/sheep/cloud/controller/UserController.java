package com.sheep.cloud.controller;

import com.sheep.cloud.request.IUsersLoginVO;
import com.sheep.cloud.request.IUsersRegisterVO;
import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.controller
 * @datetime 2022/8/12 星期五
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ApiResult doLogin(@RequestBody @Valid IUsersLoginVO dto) {
        return userService.doLogin(dto);
    }

    @PostMapping("/register")
    public ApiResult doRegister(@RequestBody @Valid IUsersRegisterVO dto) {
        return userService.doRegister(dto);
    }
}
