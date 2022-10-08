package com.sheep.cloud.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;
import com.sheep.cloud.common.CommonFields;
import com.sheep.cloud.dto.request.*;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.VerifyCodeData;
import com.sheep.cloud.service.IRemoteUserService;
import com.sheep.cloud.service.IUserService;
import com.sheep.cloud.utils.RedisUtil;
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
    private IUserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @ApiImplicitParam(name = "vo", value = "用户注册信息", required = true, dataType = "IUsersRegisterParam")
    @ApiOperation(value = "测试远程调用", notes = "测试远程调用")
    @PostMapping("/test")
    public ApiResult doRemoteRegister(@RequestBody @Valid IUsersRegisterParam vo) {
        return remoteUserService.doRemoteRegister(vo);
    }

    @ApiImplicitParam(name = "vo", value = "用户注册信息", required = true, dataType = "IUsersRegisterParam")
    @ApiOperation(value = "用户本站注册", notes = "用户本站注册")
    @PostMapping("/doRegister")
    public ApiResult doRegister(@RequestBody @Valid IUsersRegisterParam vo) {
        return userService.doRegister(vo);
    }

    @ApiImplicitParam(name = "code", value = "钉钉授权码", required = true, dataType = "String")
    @ApiOperation(value = "通过钉钉授权码登录", notes = "通过钉钉授权码登录")
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
    public ApiResult sendResetPasswordMail(HttpServletRequest request, @RequestParam(value = "email") String email) {
        if (!StringUtils.hasText(email)) {
            return ApiResult.error("请输入邮箱");
        }
        String verifyCode = RandomUtil.randomString(5).toUpperCase();
        String requestCode = RandomUtil.randomString(10).toUpperCase();
        VerifyCodeData data = new VerifyCodeData(verifyCode, requestCode);
        // 验证码三分钟内有效
        redisUtil.set(requestCode, verifyCode, 180);
        String content = "您正在找回密码,验证码为:" + verifyCode + "\n验证码有效时间3分钟，请及时处理";
        MailUtil.send(email, CommonFields.FIND_PWD_MAIL_TITLE, content, false);
        return ApiResult.success("邮件发送成功", data);
    }

    @ApiImplicitParam(name = "vo", value = "重置密码参数", required = true, dataType = "ResetPasswordVO")
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @PostMapping("/mail/reset/pwd")
    public ApiResult resetPassword(HttpServletRequest request, @RequestBody @Valid ResetPasswordParam vo) {
        return userService.resetPassword(request, vo);
    }

    @ApiImplicitParam(name = "param", value = "登录参数", required = true, dataType = "UserLoginParam")
    @ApiOperation(value = "用户登录", notes = "用户登录")
    @PostMapping("/login")
    public ApiResult doLogin(@RequestBody @Valid UserLoginParam param) {
        return userService.doLogin(param);
    }

    @PostMapping("/main/doLogin")
    @ApiImplicitParam(name = "code", value = "主站授权码", required = true, dataType = "String")
    @ApiOperation(value = "通过主站授权码登录", notes = "通过主站授权码登录")
    public ApiResult doMainWebLogin(@RequestParam(required = false) String code) {
        return userService.doMainWebLogin(code);
    }

    @ApiOperation(value = "绑定主站账号", notes = "绑定主站账号")
    @ApiImplicitParam(name = "param", value = "绑定主站账号参数", required = true, dataType = "BindMainWebAccountParam")
    @PostMapping("/main/bindAccount")
    public ApiResult doBindMainWebAccount(@RequestBody @Valid BindMainWebAccountParam param) {
        return userService.doBindMainWebAccount(param);
    }
}
