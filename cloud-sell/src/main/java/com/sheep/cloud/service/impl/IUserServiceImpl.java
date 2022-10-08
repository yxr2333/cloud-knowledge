package com.sheep.cloud.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sheep.cloud.common.CommonFields;
import com.sheep.cloud.dto.request.*;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.DingUserInfo;
import com.sheep.cloud.dto.response.TokenInfo;
import com.sheep.cloud.model.IUserEntity;
import com.sheep.cloud.repository.IUserEntityRepository;
import com.sheep.cloud.service.IRemoteOAuth2Service;
import com.sheep.cloud.service.IRemoteUserService;
import com.sheep.cloud.service.IUserService;
import com.sheep.cloud.utils.RedisUtil;
import com.sheep.cloud.utils.SignatureUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service.impl
 * @datetime 2022/9/16 星期五
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class IUserServiceImpl implements IUserService {

    private final IRemoteUserService remoteUserService;
    private final IRemoteOAuth2Service auth2Service;
    private final IUserEntityRepository userEntityRepository;
    private final SignatureUtil signatureUtil;
    private final RedisUtil redisUtil;
    private String appKey;
    private String appSecret;

    @PostConstruct
    public void initApp() throws IOException {
        InputStream stream = getClass().getResourceAsStream("/ding.properties");
        Properties properties = new Properties();
        properties.load(stream);
        appKey = properties.getProperty("appKey");
        appSecret = properties.getProperty("appSecret");
        log.info("初始化钉钉配置成功,appKey: {}, appSecret: {}", appKey, appSecret);
    }

    @Override
    public ApiResult doRegister(IUsersRegisterParam vo) {
        ApiResult result = remoteUserService.doRemoteRegister(vo);
        if (result.code == HttpStatus.HTTP_OK
                && StringUtils.hasText(result.msg)
                && result.data instanceof IUsersRegisterParam) {
            log.info("远程调用成功");
            IUsersRegisterParam data = (IUsersRegisterParam) result.data;
            IUserEntity entity = new IUserEntity();
            entity.setUsername(data.getUsername());
            entity.setPassword(data.getPassword());
            entity.setSalt(result.msg);
            entity.setEmail(data.getEmail());
            entity.setDescription(data.getDescription() == null ? "" : data.getDescription());
            userEntityRepository.save(entity);
            return ApiResult.success("注册成功");
        } else {
            return ApiResult.error("暂时无法进行处理");
        }
    }

    /**
     * 钉钉授权登录
     *
     * @param code 钉钉授权码
     * @return 用户信息
     */
    @Override
    public ApiResult doDingLogin(String code) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String signature = signatureUtil.makeDingSignature(timestamp, appSecret);
        HashMap<String, String> args = new HashMap<>(2);
        args.put("tmp_auth_code", code);
        String body = JSONUtil.toJsonStr(args);
        try (HttpResponse response = HttpRequest.post(CommonFields.USER_INFO_CODE_URL + "?accessKey=" + appKey + "&timestamp=" + timestamp + "&signature=" + signature)
                .body(body)
                .execute()) {
            // 判断钉钉接口调用是否成功
            if (response.isOk()) {
                JSONObject obj = JSONUtil.parseObj(response.body());
                if (response.getStatus() == HttpStatus.HTTP_OK && obj.getInt("errcode") == 0 && "ok".equals(obj.getStr("errmsg"))) {
                    JSONObject jsonUserInfo = obj.getJSONObject("user_info");
                    DingUserInfo userInfo = new DingUserInfo();
                    userInfo.setNickname(jsonUserInfo.getStr("nick"));
                    userInfo.setOpenId(jsonUserInfo.getStr("openid"));
                    userInfo.setUnionId(jsonUserInfo.getStr("unionid"));
                    // 判断appId是否绑定了账号
                    Optional<IUserEntity> optionalIUser = userEntityRepository.findByDingAppId(userInfo.getOpenId());
                    // 如果没有绑定账号
                    if (!optionalIUser.isPresent() || !optionalIUser.get().getIsBindDing()) {
                        return ApiResult.notBind("该钉钉账号未绑定账号,请先进行账号绑定", userInfo);
                    } else {
                        StpUtil.login(optionalIUser.get().getId() + "");
                        String tokenName = StpUtil.getTokenInfo().getTokenName();
                        String tokenValue = StpUtil.getTokenInfo().getTokenValue();
                        TokenInfo tokenInfo = new TokenInfo(tokenName, tokenValue);
                        return ApiResult.success("登录成功", tokenInfo);
                    }
                } else {
                    return ApiResult.error(obj.get("errmsg").toString());
                }
            } else {
                return ApiResult.error("暂时无法进行处理");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.error("暂时无法进行处理");
        }
    }

    /**
     * 绑定钉钉账号
     *
     * @param param 参数
     * @return 绑定结果
     */
    @Override
    public ApiResult bindDingAccount(BindDingAccountParam param) {
        Boolean isExist = userEntityRepository.existsByDingAppId(param.getOpenid());
        if (isExist) {
            return ApiResult.error("该钉钉账号已经绑定了账号");
        }
        if (userEntityRepository.existsByUsername(param.getUsername())) {
            return ApiResult.error("该用户名已经被使用");
        }
        IUsersRegisterParam remoteParam = new IUsersRegisterParam(param.getUsername(), param.getPassword(), param.getEmail(), param.getDescription());
        // 远程调用注册接口
        ApiResult remoteRegisterResult = remoteUserService.doRemoteRegister(remoteParam);
        log.info("远程调用注册接口返回结果:{}", remoteRegisterResult);
        if (remoteRegisterResult.code == HttpStatus.HTTP_OK
                && StringUtils.hasText(remoteRegisterResult.msg)) {
            // 获取加密后的一些信息
            JSONObject jsonData = JSONUtil.parseObj(remoteRegisterResult.data);
            IUserEntity entity = new IUserEntity();
            // 设置参数，保存到数据库
            entity.setUsername(jsonData.getStr("username"));
            entity.setPassword(jsonData.getStr("password"));
            entity.setSalt(remoteRegisterResult.msg);
            entity.setDescription(jsonData.getStr("description") == null ? "" : jsonData.getStr("description"));
            entity.setEmail(jsonData.getStr("email"));
            entity.setDingAppId(param.getOpenid());
            entity.setIsBindDing(true);
            entity.setIsBanned(false);
            userEntityRepository.save(entity);
            return ApiResult.success("绑定成功");
        } else {
            return ApiResult.error("暂时无法进行处理");
        }
    }


    /**
     * 重置密码
     *
     * @param request 请求对象
     * @param vo      重置密码信息
     * @return 重置结果
     */
    @Override
    public ApiResult resetPassword(HttpServletRequest request, ResetPasswordParam vo) {
        String requestKey = vo.getRequestKey();
        Object o = redisUtil.get(requestKey);
        if (o == null) {
            return ApiResult.error("未获取验证码或验证码已过期");
        }
        String resetCode = o.toString();
        if (resetCode.equals(vo.getCode())) {
            // 判断账号密码是否匹配
            IUserEntity entity = checkPassword(vo.getUsername(), vo.getPassword());
            if (entity != null) {
                HashMap<String, String> map = makeSalt(vo.getNewPassword());
                entity.setSalt(map.get("salt"));
                entity.setPassword(map.get("password"));
                userEntityRepository.save(entity);
                // 删除验证码
                redisUtil.delete(requestKey);
                return ApiResult.success("重置密码成功");
            } else {
                return ApiResult.error("旧密码错误");
            }
        } else {
            return ApiResult.error("验证码错误");
        }
    }

    private HashMap<String, String> makeSalt(String password) {
        String randomString = RandomUtil.randomString(10);
        String salt = Base64.encode(randomString);
        String encodedPassword = SecureUtil.md5(password + salt);
        HashMap<String, String> map = new HashMap<>(2);
        map.put("salt", salt);
        map.put("password", encodedPassword);
        return map;
    }

    /**
     * 用户登录
     *
     * @param param 登录参数
     * @return 登录结果
     */
    @Override
    public ApiResult doLogin(UserLoginParam param) {

        IUserEntity entity = checkPassword(param.getUsername(), param.getPassword());
        if (entity != null) {
            StpUtil.login(entity.getId() + "");
            String tokenName = StpUtil.getTokenInfo().getTokenName();
            String tokenValue = StpUtil.getTokenInfo().getTokenValue();
            TokenInfo tokenInfo = new TokenInfo(tokenName, tokenValue);
            return ApiResult.success("登录成功", tokenInfo);
        } else {
            return ApiResult.error("用户名或密码错误");
        }
    }

    /**
     * 判断账号密码是否匹配
     *
     * @param username 用户名
     * @param password 密码
     * @return 匹配结果, 匹配成功返回用户信息, 匹配失败返回null
     */
    private IUserEntity checkPassword(String username, String password) {
        IUserEntity entity = userEntityRepository.findDistinctByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        String salt = entity.getSalt();
        String pwd = SecureUtil.md5(password + salt);
        if (pwd.equals(entity.getPassword())) {
            return entity;
        } else {
            return null;
        }
    }

    /**
     * 通过主站登录
     *
     * @param code 主站授权码
     * @return 登录结果
     */
    @Override
    public ApiResult doMainWebLogin(String code) {
        if (!StringUtils.hasText(code)) {
            return ApiResult.error("授权码不能为空");
        }
        HttpResponse response = HttpRequest.get(CommonFields.MAIN_WEB_USER_APPID_URL + "&code=" + code).execute();
        if (!response.isOk()) {
            return ApiResult.error("暂时无法获取用户信息");
        } else {
            // body就是data
            JSONObject data = JSONUtil.parseObj(response.body());
            log.info("获取到的用户信息: {}", data);
            if (data.getInt("code") == HttpStatus.HTTP_OK) {
                String accessToken = data.getJSONObject("data").getStr("access_token");
                String openId = data.getJSONObject("data").getStr("openid");
                Optional<IUserEntity> optional = userEntityRepository.findByMainAccountAppId(openId);
                if (!optional.isPresent()) {
                    HashMap<String, String> map = new HashMap<>(2);
                    map.put("access_token", accessToken);
                    map.put("openid", openId);
                    return ApiResult.notBind("未绑定主站账号", map);
                } else {
                    IUserEntity entity = optional.get();
                    StpUtil.login(entity.getId() + "");
                    String tokenName = StpUtil.getTokenInfo().getTokenName();
                    String tokenValue = StpUtil.getTokenInfo().getTokenValue();
                    TokenInfo tokenInfo = new TokenInfo(tokenName, tokenValue);
                    return ApiResult.success("登录成功", tokenInfo);
                }
            } else {
                return ApiResult.error("统一认证中心暂不能处理");
            }
        }
    }

    /**
     * 绑定主站账号
     *
     * @param param 参数
     * @return 绑定结果
     */
    @Override
    public ApiResult doBindMainWebAccount(BindMainWebAccountParam param) {
        if (userEntityRepository.existsByMainAccountAppId(param.getOpenid())) {
            return ApiResult.error("该主站账号已被绑定");
        }
        if (userEntityRepository.existsByUsername(param.getUsername())) {
            return ApiResult.error("该用户名已被注册");
        }
        if (userEntityRepository.existsByEmail(param.getEmail())) {
            return ApiResult.error("该邮箱已被其他账户使用");
        }
        ApiResult result = auth2Service.oauth2UserInfo(param.getAccessToken());
        if (result.code == HttpStatus.HTTP_OK) {
            Integer uid = JSONUtil.parseObj(result.data).getInt("uid");
            HashMap<String, String> map = makeSalt(param.getPassword());
            IUserEntity entity = IUserEntity.builder()
                    .isBindMainAccount(true)
                    .mainAccountId(uid)
                    .mainAccountAppId(param.getOpenid())
                    .username(param.getUsername())
                    .password(map.get("password"))
                    .salt(map.get("salt"))
                    .email(param.getEmail())
                    .description(param.getDescription())
                    .isBanned(false)
                    .build();
            userEntityRepository.save(entity);
            return ApiResult.success("绑定成功");
        } else {
            return result;
        }
    }
}
