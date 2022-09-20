package com.sheep.cloud.service;


import com.sheep.cloud.dto.request.BindDingAccountParam;
import com.sheep.cloud.dto.request.IUsersRegisterVO;
import com.sheep.cloud.dto.request.ResetPasswordVO;
import com.sheep.cloud.dto.request.UserLoginParam;
import com.sheep.cloud.dto.response.ApiResult;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service
 * @datetime 2022/9/16 星期五
 */
public interface IUserService {

    ApiResult doRegister(IUsersRegisterVO vo);

    /**
     * 钉钉授权登录
     *
     * @param code 钉钉授权码
     * @return 用户信息
     */
    ApiResult doDingLogin(String code) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException;

    /**
     * 绑定钉钉账号
     *
     * @param param 参数
     * @return 绑定结果
     */
    ApiResult bindDingAccount(BindDingAccountParam param);

    /**
     * 重置密码
     *
     * @param request 请求对象
     * @param vo      重置密码信息
     * @return 重置结果
     */
    ApiResult resetPassword(HttpServletRequest request, ResetPasswordVO vo);

    /**
     * 用户登录
     *
     * @param param 登录参数
     * @return 登录结果
     */
    ApiResult doLogin(UserLoginParam param);
}
