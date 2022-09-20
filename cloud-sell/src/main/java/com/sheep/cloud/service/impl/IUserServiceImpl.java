package com.sheep.cloud.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sheep.cloud.dto.request.BindDingAccountParam;
import com.sheep.cloud.dto.request.IUsersRegisterVO;
import com.sheep.cloud.dto.request.ResetPasswordVO;
import com.sheep.cloud.dto.request.UserLoginParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.DingUserInfo;
import com.sheep.cloud.dto.response.TokenInfo;
import com.sheep.cloud.model.IUserEntity;
import com.sheep.cloud.repository.IUserEntityRepository;
import com.sheep.cloud.service.IRemoteUserService;
import com.sheep.cloud.service.IUserService;
import com.sheep.cloud.utils.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service.impl
 * @datetime 2022/9/16 星期五
 */
@Service
@Slf4j
public class IUserServiceImpl implements IUserService {

    public static final String USER_INFO_CODE_URL = "https://oapi.dingtalk.com/sns/getuserinfo_bycode";
    @Autowired
    private IRemoteUserService remoteUserService;

    @Autowired
    private IUserEntityRepository userEntityRepository;

    @Autowired
    private SignatureUtil signatureUtil;

    @Value("${dingtalk.appKey}")
    private String appKey;

    @Value("${dingtalk.appSecret}")
    private String appSecret;


    @Override
    public ApiResult doRegister(IUsersRegisterVO vo) {
        ApiResult result = remoteUserService.doRemoteRegister(vo);
        if (result.code == HttpStatus.HTTP_OK
                && StringUtils.hasText(result.msg)
                && result.data instanceof IUsersRegisterVO) {
            log.info("远程调用成功");
            IUsersRegisterVO data = (IUsersRegisterVO) result.data;
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
        HttpResponse response = HttpRequest.post(USER_INFO_CODE_URL + "?accessKey=" + appKey + "&timestamp=" + timestamp + "&signature=" + signature)
                .body(body)
                .execute();
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
        IUsersRegisterVO remoteParam = new IUsersRegisterVO(param.getUsername(), param.getPassword(), param.getEmail(), param.getDescription());
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
    public ApiResult resetPassword(HttpServletRequest request, ResetPasswordVO vo) {
        return null;
    }

    /**
     * 用户登录
     *
     * @param param 登录参数
     * @return 登录结果
     */
    @Override
    public ApiResult doLogin(UserLoginParam param) {
        return null;
    }
}
