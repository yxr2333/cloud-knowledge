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
import com.sheep.cloud.dao.sell.ISellUserEntityRepository;
import com.sheep.cloud.dto.request.knowledge.IUsersLoginVO;
import com.sheep.cloud.dto.request.knowledge.IUsersRegisterVO;
import com.sheep.cloud.dto.request.sell.BindDingAccountParam;
import com.sheep.cloud.dto.request.sell.BindMainWebAccountParam;
import com.sheep.cloud.dto.request.sell.ResetPasswordParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.OAuth2TokenInfo;
import com.sheep.cloud.dto.response.knowledge.IUsersBaseInfoDTO;
import com.sheep.cloud.dto.response.sell.DingUserInfo;
import com.sheep.cloud.dto.response.sell.IUserEntityBaseInfo;
import com.sheep.cloud.dto.response.sell.IUserLoginDTO;
import com.sheep.cloud.dto.response.sell.TokenInfo;
import com.sheep.cloud.entity.sell.ISellUserEntity;
import com.sheep.cloud.entity.sell.ISellUserRoleEntity;
import com.sheep.cloud.service.IRemoteOAuth2Service;
import com.sheep.cloud.service.IRemoteUserService;
import com.sheep.cloud.service.IUserService;
import com.sheep.cloud.store.RedisUtil;
import com.sheep.cloud.utils.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
 * @datetime 2022/9/16 ?????????
 */
@Service
@Slf4j
public class IUserServiceImpl implements IUserService {

    @Autowired
    private IRemoteUserService remoteUserService;
    @Autowired
    private IRemoteOAuth2Service auth2Service;
    @Autowired
    private ISellUserEntityRepository userEntityRepository;
    @Autowired
    private SignatureUtil signatureUtil;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISellUserRoleEntity normalUser;
    @Value("${sso.grantType}")
    private String grantType;
    @Value("${sso.clientId}")
    private String clientId;
    @Value("${sso.clientSecret}")
    private String clientSecret;
    private String appKey;
    private String appSecret;

    @PostConstruct
    public void initApp() throws IOException {
        InputStream stream = getClass().getResourceAsStream("/ding.properties");
        Properties properties = new Properties();
        properties.load(stream);
        appKey = properties.getProperty("appKey");
        appSecret = properties.getProperty("appSecret");
        log.info("???????????????????????????,appKey: {}, appSecret: {}", appKey, appSecret);
    }

    /**
     * ?????????????????????
     *
     * @param param ????????????
     * @return ????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> doAdminLogin(IUsersLoginVO param) {
        ISellUserEntity entity = checkPassword(param.getUsername(), param.getPassword());
        if (entity == null) {
            throw new RuntimeException("????????????????????????");
        } else {
            if (entity.getRole().getId().equals(normalUser.getId())) {
                throw new RuntimeException("???????????????????????????");
            } else {
                IUserEntityBaseInfo baseInfo = modelMapper.map(entity, IUserEntityBaseInfo.class);
                return new ApiResult<IUserEntityBaseInfo>().success("????????????", baseInfo);
            }
        }
    }

    @Override
    public ApiResult<?> doRegister(IUsersRegisterVO vo) {
        ApiResult<IUsersRegisterVO> result = remoteUserService.doRemoteRegister(vo);
        if (result.code == HttpStatus.HTTP_OK
                && StringUtils.hasText(result.msg)) {
            log.info("??????????????????");
            IUsersRegisterVO ansVo = result.getData();
            ISellUserEntity entity = ISellUserEntity.builder()
                    .username(ansVo.getUsername())
                    .password(ansVo.getPassword())
                    .salt(result.msg)
                    .email(ansVo.getEmail())
                    .description(ansVo.getDescription() == null ? "" : ansVo.getDescription())
                    .isBanned(false)
                    .isBindMainAccount(false)
                    .isBindDing(false)
                    .role(normalUser)
                    .freeMoney(0.0)
                    .build();
            userEntityRepository.save(entity);
            return new ApiResult<>().success("????????????");
        } else {
            log.error(result.toString());
            return new ApiResult<>().error("????????????????????????", result);
        }
    }

    /**
     * ??????????????????
     *
     * @param code ???????????????
     * @return ????????????
     */
    @Override
    public ApiResult<?> doDingLogin(String code) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String signature = signatureUtil.makeDingSignature(timestamp, appSecret);
        HashMap<String, String> args = new HashMap<>(2);
        args.put("tmp_auth_code", code);
        String body = JSONUtil.toJsonStr(args);
        try (HttpResponse response = HttpRequest.post(CommonFields.USER_INFO_CODE_URL + "?accessKey=" + appKey + "&timestamp=" + timestamp + "&signature=" + signature)
                .body(body)
                .execute()) {
            // ????????????????????????????????????
            if (response.isOk()) {
                JSONObject obj = JSONUtil.parseObj(response.body());
                if (response.getStatus() == HttpStatus.HTTP_OK && obj.getInt("errcode") == 0 && "ok".equals(obj.getStr("errmsg"))) {
                    JSONObject jsonUserInfo = obj.getJSONObject("user_info");
                    DingUserInfo userInfo = new DingUserInfo();
                    userInfo.setNickname(jsonUserInfo.getStr("nick"));
                    userInfo.setOpenId(jsonUserInfo.getStr("openid"));
                    userInfo.setUnionId(jsonUserInfo.getStr("unionid"));
                    // ??????appId?????????????????????
                    Optional<ISellUserEntity> optionalUser = userEntityRepository.findByDingAppId(userInfo.getOpenId());
                    // ????????????????????????
                    if (!optionalUser.isPresent() || !optionalUser.get().getIsBindDing()) {
                        return new ApiResult<>().notBind("??????????????????????????????,????????????????????????", userInfo);
                    } else {
                        StpUtil.login(optionalUser.get().getId() + "");
                        String tokenName = StpUtil.getTokenInfo().getTokenName();
                        String tokenValue = StpUtil.getTokenInfo().getTokenValue();
                        TokenInfo tokenInfo = new TokenInfo(tokenName, tokenValue);
                        return new ApiResult<>().success("????????????", tokenInfo);
                    }
                } else {
                    return new ApiResult<>().error(obj.get("errmsg").toString());
                }
            } else {
                return new ApiResult<>().error("????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResult<>().error("????????????????????????");
        }
    }

    /**
     * ??????????????????
     *
     * @param param ??????
     * @return ????????????
     */
    @Override
    public ApiResult<?> bindDingAccount(BindDingAccountParam param) {
        Boolean isExist = userEntityRepository.existsByDingAppId(param.getOpenid());
        if (isExist) {
            return new ApiResult<>().error("????????????????????????????????????");
        }
        if (userEntityRepository.existsByUsername(param.getUsername())) {
            return new ApiResult<>().error("???????????????????????????");
        }
        IUsersRegisterVO remoteParam = new IUsersRegisterVO(param.getUsername(), param.getPassword(), param.getEmail(), param.getDescription());
        // ????????????????????????
        ApiResult<IUsersRegisterVO> remoteRegisterResult = remoteUserService.doRemoteRegister(remoteParam);
        log.info("????????????????????????????????????:{}", remoteRegisterResult);
        if (remoteRegisterResult.code == HttpStatus.HTTP_OK
                && StringUtils.hasText(remoteRegisterResult.msg)) {
            // ??????????????????????????????
            IUsersRegisterVO data = remoteRegisterResult.getData();
            ISellUserEntity entity = new ISellUserEntity();
            // ?????????????????????????????????
            entity.setUsername(data.getUsername());
            entity.setPassword(data.getPassword());
            entity.setSalt(remoteRegisterResult.msg);
            entity.setDescription(data.getDescription() == null ? "" : data.getDescription());
            entity.setEmail(data.getEmail());
            entity.setDingAppId(param.getOpenid());
            entity.setIsBindDing(true);
            entity.setIsBanned(false);
            userEntityRepository.save(entity);
            return new ApiResult<>().success("????????????");
        } else {
            return new ApiResult<>().error("????????????????????????");
        }
    }


    /**
     * ????????????
     *
     * @param request ????????????
     * @param vo      ??????????????????
     * @return ????????????
     */
    @Override
    public ApiResult<?> resetPassword(HttpServletRequest request, ResetPasswordParam vo) {
        String requestKey = vo.getRequestKey();
        Object o = redisUtil.get(requestKey);
        if (o == null) {
            return new ApiResult<>().error("???????????????????????????????????????");
        }
        String resetCode = o.toString();
        if (resetCode.equals(vo.getCode())) {
            // ??????????????????????????????
            ISellUserEntity entity = checkPassword(vo.getUsername(), vo.getPassword());
            if (entity != null) {
                HashMap<String, String> map = makeSalt(vo.getNewPassword());
                entity.setSalt(map.get("salt"));
                entity.setPassword(map.get("password"));
                userEntityRepository.save(entity);
                // ???????????????
                redisUtil.delete(requestKey);
                return new ApiResult<>().success("??????????????????");
            } else {
                return new ApiResult<>().error("???????????????");
            }
        } else {
            return new ApiResult<>().error("???????????????");
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
     * ????????????
     *
     * @param param ????????????
     * @return ????????????
     */
    @Override
    public ApiResult<?> doLogin(IUsersLoginVO param) {
        ISellUserEntity entity = checkPassword(param.getUsername(), param.getPassword());
        if (entity != null) {
            StpUtil.login(entity.getId() + "");
            String tokenName = StpUtil.getTokenInfo().getTokenName();
            String tokenValue = StpUtil.getTokenInfo().getTokenValue();
            TokenInfo tokenInfo = new TokenInfo(tokenName, tokenValue);
            IUserEntityBaseInfo baseInfo = modelMapper.map(entity, IUserEntityBaseInfo.class);
            IUserLoginDTO loginDTO = new IUserLoginDTO(tokenInfo, baseInfo);
            return new ApiResult<IUserLoginDTO>().success("????????????", loginDTO);
        } else {
            return new ApiResult<>().error("????????????????????????");
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param username ?????????
     * @param password ??????
     * @return ????????????, ??????????????????????????????, ??????????????????null
     */
    private ISellUserEntity checkPassword(String username, String password) {
        ISellUserEntity entity = userEntityRepository.findAllByUsername(username)
                .orElseThrow(() -> new RuntimeException("???????????????"));
        String salt = entity.getSalt();
        String pwd = SecureUtil.md5(password + salt);
        if (pwd.equals(entity.getPassword())) {
            return entity;
        } else {
            return null;
        }
    }

    /**
     * ??????????????????
     *
     * @param code ???????????????
     * @return ????????????
     */
    @Override
    public ApiResult<?> doMainWebLogin(String code) {
        if (!StringUtils.hasText(code)) {
            return new ApiResult<>().error("?????????????????????");
        }
        // TODO: ?????????RPC??????
        ApiResult<OAuth2TokenInfo> result = auth2Service.oauth2Token(grantType, clientId, clientSecret, code);
        if (result.code == 200) {
            OAuth2TokenInfo data = result.getData();
            log.info("????????????????????????: {}", data);
            String accessToken = data.getAccessToken();
            String openId = data.getOpenId();
            Optional<ISellUserEntity> optional = userEntityRepository.findByMainAccountAppId(openId);
            if (!optional.isPresent()) {
                HashMap<String, String> map = new HashMap<>(2);
                map.put("access_token", accessToken);
                map.put("openid", openId);
                return new ApiResult<>().notBind("?????????????????????", map);
            } else {
                ISellUserEntity entity = optional.get();
                StpUtil.login(entity.getId() + "");
                String tokenName = StpUtil.getTokenInfo().getTokenName();
                String tokenValue = StpUtil.getTokenInfo().getTokenValue();
                TokenInfo tokenInfo = new TokenInfo(tokenName, tokenValue);
                return new ApiResult<>().success("????????????", tokenInfo);
            }
        } else {
            return new ApiResult<>().error("?????????????????????????????????");
        }
    }

    /**
     * ??????????????????
     *
     * @param param ??????
     * @return ????????????
     */
    @Override
    public ApiResult<?> doBindMainWebAccount(BindMainWebAccountParam param) {
        if (userEntityRepository.existsByMainAccountAppId(param.getOpenid())) {
            return new ApiResult<>().error("???????????????????????????");
        }
        if (userEntityRepository.existsByEmail(param.getEmail())) {
            return new ApiResult<>().error("?????????????????????????????????");
        }
        ISellUserEntity userEntity = null;
        Optional<ISellUserEntity> optional = userEntityRepository.findAllByUsername(param.getUsername());
        if (optional.isPresent()) {
            userEntity = optional.get();
        }
        ApiResult<IUsersBaseInfoDTO> result = auth2Service.oauth2UserInfo(param.getAccessToken());
        if (result.code == HttpStatus.HTTP_OK) {
            Integer uid = result.getData().getUid();
            HashMap<String, String> map = makeSalt(param.getPassword());
            // ??????????????????????????????????????????????????????????????????????????????????????????
            if (userEntity != null) {
                userEntity.setIsBindMainAccount(true);
                userEntity.setMainAccountAppId(param.getOpenid());
                userEntity.setMainAccountId(uid);
                userEntityRepository.save(userEntity);
            } else {
                ISellUserEntity entity = ISellUserEntity.builder()
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
            }

            return new ApiResult<>().success("????????????");
        } else {
            return result;
        }
    }
}
