package com.sheep.cloud.controller;

import com.sheep.cloud.dto.request.BindDingAccountParam;
import com.sheep.cloud.dto.request.IUsersRegisterVO;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.IRemoteMailService;
import com.sheep.cloud.service.IRemoteUserService;
import com.sheep.cloud.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.controller
 * @datetime 2022/9/16 星期五
 */
@RequestMapping("/user")
@RestController
@Api(tags = "用户模块")
public class IUserController {

    @Autowired
    private IRemoteUserService remoteUserService;

    @Autowired
    private IRemoteMailService remoteMailService;
    @Autowired
    private IUserService userService;

    @ApiImplicitParam(name = "vo", value = "用户注册信息", required = true, dataType = "IUsersRegisterVO")
    @ApiOperation(value = "测试远程调用", notes = "测试远程调用")
    @PostMapping("/test")
    public ApiResult doRemoteRegister(@RequestBody @Valid IUsersRegisterVO vo) {
        return remoteUserService.doRemoteRegister(vo);
    }

    @ApiImplicitParam(name = "vo", value = "用户注册信息", required = true, dataType = "IUsersRegisterVO")
    @ApiOperation(value = "用户本站注册", notes = "用户本站注册")
    @PostMapping("/doRegister")
    public ApiResult doRegister(@RequestBody @Valid IUsersRegisterVO vo) {
        return userService.doRegister(vo);
    }

    @ApiImplicitParam(name = "code", value = "钉钉授权码", required = true, dataType = "String")
    @PostMapping("/ding/doLogin")
    public ApiResult doDingLogin(@RequestParam(required = false) String code) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        if (!StringUtils.hasText(code)) {
            return ApiResult.error("未识别到授权码");
        }
        return userService.doDingLogin(code);
    }

    @ApiImplicitParam(name = "param", value = "绑定钉钉账号参数", required = true, dataType = "BindDingAccountParam")
    @ApiOperation(value = "绑定钉钉账号", notes = "绑定钉钉账号")
    @PostMapping("/ding/bindAccount")
    public ApiResult bindDingAccount(@RequestBody @Valid BindDingAccountParam param) {
        return userService.bindDingAccount(param);
    }

    @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String")
    @ApiOperation(value = "发送找回密码的邮件", notes = "发送找回密码的邮件")
    @PostMapping("/mail/send/pwd")
    public ApiResult sendResetPasswordMail(HttpServletRequest request, String email) {
        return remoteMailService.sendResetCode(request, email);
    }
}
