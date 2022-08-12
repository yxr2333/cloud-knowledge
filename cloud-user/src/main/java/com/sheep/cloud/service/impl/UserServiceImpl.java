package com.sheep.cloud.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.SecureUtil;
import com.sheep.cloud.dao.IUsersEntityRepository;
import com.sheep.cloud.entity.IUsersEntity;
import com.sheep.cloud.request.IUsersLoginVO;
import com.sheep.cloud.request.IUsersModifyInfoVO;
import com.sheep.cloud.request.IUsersRegisterVO;
import com.sheep.cloud.request.IUsersResetPasswordVO;
import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service.impl
 * @datetime 2022/8/12 星期五
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private IUsersEntityRepository usersEntityRepository;

    /**
     * 用户登录
     *
     * @param dto 用户登录信息
     * @return 登录结果
     */
    @Override
    public ApiResult doLogin(IUsersLoginVO dto) {
        IUsersEntity entity = usersEntityRepository.findIUsersEntityByUsername(dto.getUsername()).orElseThrow(() -> new RuntimeException("用户不存在"));
        String salt = entity.getSalt();
        String password = SecureUtil.md5(dto.getPassword() + salt);
        log.info("password:{}", password);
        log.info("entity.getPassword():{}", entity.getPassword());
        if (password.equals(entity.getPassword())) {
            StpUtil.login(entity.getUid() + entity.getUsername());
            Map<String, String> token = MapUtil.builder(new HashMap<String, String>())
                    .put("token", StpUtil.getTokenValue())
                    .put("tokenName", StpUtil.getTokenName())
                    .build();
            return ApiResult.success("登录成功", token);
        } else {
            return ApiResult.error("用户名或密码错误");
        }
    }

    /**
     * 用户注册
     *
     * @param dto 用户注册信息
     * @return 注册结果
     */
    @Override
    public ApiResult doRegister(IUsersRegisterVO dto) {
        if (usersEntityRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        String randomString = RandomUtil.randomString(10);
        String salt = Base64.encode(randomString);
        String password = SecureUtil.md5(dto.getPassword() + salt);
        // 将dto转换为entity
        IUsersEntity entity = new IUsersEntity();
        entity.setUsername(dto.getUsername());
        entity.setPassword(password);
        entity.setSalt(salt);
        entity.setEmail(dto.getEmail());
        entity.setDescription(dto.getDescription() == null ? "" : dto.getDescription());
        usersEntityRepository.save(entity);
        return ApiResult.success("注册成功");
    }


    /**
     * 用户重置密码
     *
     * @param request         请求对象
     * @param resetPasswordVO 用户重置密码信息
     * @return 重置密码结果
     */
    public ApiResult resetPassword(HttpServletRequest request, IUsersResetPasswordVO resetPasswordVO) {
        Object resetCode = request.getSession().getAttribute("reset_code");
        if (resetCode == null) {
            return ApiResult.error("未发送验证码或验证码已过期");
        }
        if (!resetCode.toString().equals(resetPasswordVO.getCode())) {
            return ApiResult.error("验证码错误");
        }
        String randomString = RandomUtil.randomString(10);
        String salt = Base64.encode(randomString);
        String password = SecureUtil.md5(resetPasswordVO.getNewPassword() + salt);
        IUsersEntity entity = usersEntityRepository.findIUsersEntityByUsername(resetPasswordVO.getUsername()).orElseThrow(() -> new RuntimeException("用户不存在"));
        // 重新设置密码
        entity.setSalt(salt);
        entity.setPassword(password);
        // 删除验证码
        request.setAttribute("reset_code", null);

        return ApiResult.success("重置密码成功");
    }

    /**
     * 用户修改信息
     *
     * @param modifyInfoVO 用户修改信息
     * @return 修改结果
     */
    @Override
    public ApiResult modifyInfo(IUsersModifyInfoVO modifyInfoVO) {
        IUsersEntity entity = usersEntityRepository
                .findById(modifyInfoVO.getId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        if (StringUtils.hasText(modifyInfoVO.getEmail())) {
            boolean match = ReUtil.isMatch("^(([^<>()[\\]\\\\.,;:\\s@\"]+(\\.[^<>()[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))", modifyInfoVO.getEmail());
            if (!match) {
                return ApiResult.error("邮箱格式错误");
            } else {
                entity.setEmail(modifyInfoVO.getEmail());
            }
        }
        if (StringUtils.hasText(modifyInfoVO.getAvatar())) {
            entity.setAvatar(modifyInfoVO.getAvatar());
        }
        if (StringUtils.hasText(modifyInfoVO.getUsername()) && !entity.getUsername().equals(modifyInfoVO.getUsername())) {
            if (usersEntityRepository.existsByUsername(modifyInfoVO.getUsername())) {
                return ApiResult.error("用户名已存在");
            } else {
                entity.setUsername(modifyInfoVO.getUsername());
            }
        }
        if (StringUtils.hasText(modifyInfoVO.getDescription())) {
            entity.setDescription(modifyInfoVO.getDescription());
        }
        if (!Objects.isNull(modifyInfoVO.getLabels())) {
            entity.setLabels(modifyInfoVO.getLabels());
        }
        usersEntityRepository.save(entity);
        return ApiResult.success("修改更新成功");
    }

    /**
     * 通过用户名id删除用户
     *
     * @param id 用户id
     * @return 删除结果
     */
    @Override
    public ApiResult deleteUserById(Integer id) {
        // 判断是否存在并删除
        if (usersEntityRepository.existsById(id)) {
            usersEntityRepository.deleteById(id);
            return ApiResult.success("删除成功");
        } else {
            return ApiResult.error("用户不存在");
        }
    }

    /**
     * 通过用户名id查询用户
     *
     * @param id 用户id
     * @return 查询结果
     */
    @Override
    public ApiResult getOne(Integer id) {
        IUsersEntity entity = usersEntityRepository.findById(id).orElseThrow(() -> new RuntimeException("用户不存在"));
        return ApiResult.success(entity);
    }

    /**
     * 通过用户名模糊查询用户
     *
     * @param name 用户名
     * @return 查询结果
     */
    @Override
    public ApiResult getAllLikeName(String name) {
        return ApiResult.success(usersEntityRepository.findAllByUsernameLike(name));
    }

    /**
     * 给用户添加积分
     *
     * @param score 积分
     * @param id    用户id
     * @return 添加结果
     */
    @Override
    public ApiResult addScore(Integer score, Integer id) {
        return null;
    }
}
