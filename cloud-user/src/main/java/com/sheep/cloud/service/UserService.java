package com.sheep.cloud.service;

import com.sheep.cloud.request.IUsersLoginVO;
import com.sheep.cloud.request.IUsersModifyInfoVO;
import com.sheep.cloud.request.IUsersRegisterVO;
import com.sheep.cloud.request.IUsersResetPasswordVO;
import com.sheep.cloud.response.ApiResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service
 * @datetime 2022/8/12 星期五
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param loginVO 用户登录信息
     * @return 登录结果
     */
    ApiResult doLogin(IUsersLoginVO loginVO);


    /**
     * 用户注册
     *
     * @param registerVO 用户注册信息
     * @return 注册结果
     */
    ApiResult doRegister(IUsersRegisterVO registerVO);

    /**
     * 用户重置密码
     *
     * @param request         请求对象
     * @param resetPasswordVO 用户重置密码信息
     * @return 重置密码结果
     */
    ApiResult resetPassword(HttpServletRequest request, IUsersResetPasswordVO resetPasswordVO);


    /**
     * 用户修改信息
     *
     * @param modifyInfoVO 用户修改信息
     * @return 修改结果
     */
    ApiResult modifyInfo(IUsersModifyInfoVO modifyInfoVO);

    /**
     * 通过用户名id删除用户
     *
     * @param id 用户id
     * @return 删除结果
     */
    ApiResult deleteUserById(Integer id);

    /**
     * 通过用户名id查询用户
     *
     * @param id 用户id
     * @return 查询结果
     */
    ApiResult getOne(Integer id);

    /**
     * 通过用户名模糊查询用户
     *
     * @param name 用户名
     * @return 查询结果
     */
    ApiResult getAllLikeName(String name);

    /**
     * 给用户添加积分
     *
     * @param score 积分
     * @param id    用户id
     * @return 添加结果
     */
    ApiResult addScore(Integer score, Integer id);
}
