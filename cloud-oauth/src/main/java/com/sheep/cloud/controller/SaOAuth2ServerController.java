package com.sheep.cloud.controller;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Handle;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sheep.cloud.dao.IAppClientsEntityRepository;
import com.sheep.cloud.dao.IOAuth2GrantEntityRepository;
import com.sheep.cloud.dao.IUsersEntityRepository;
import com.sheep.cloud.entity.IAppClientsEntity;
import com.sheep.cloud.entity.IOAuth2GrantEntity;
import com.sheep.cloud.entity.IUsersEntity;
import com.sheep.cloud.request.IUsersLoginVO;
import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.response.IUsersBaseInfoDTO;
import com.sheep.cloud.service.RemoteUserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Optional;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.controller
 * @datetime 2022/9/14 星期三
 */
@RestController
@Slf4j
public class SaOAuth2ServerController {

    @Autowired
    private IUsersEntityRepository usersEntityRepository;

    @Autowired
    private IOAuth2GrantEntityRepository auth2GrantEntityRepository;

    @Autowired
    private IAppClientsEntityRepository appClientsEntityRepository;
    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping("/oauth2/userinfo")
    public ApiResult oauth2UserInfo() {
        String accessToken = SaHolder.getRequest().getParamNotNull("access_token");
        Object loginId = SaOAuth2Util.getLoginIdByAccessToken(accessToken);
        System.out.println("accessToken = " + accessToken);
        SaOAuth2Util.checkScope(accessToken, "userinfo");
        Optional<IUsersEntity> optional = usersEntityRepository.findById(Integer.parseInt((String) loginId));
        return optional.map(iUsersEntity -> ApiResult.success(modelMapper.map(iUsersEntity, IUsersBaseInfoDTO.class))).orElseGet(() -> ApiResult.error("用户不存在"));
    }

    @RequestMapping("/oauth2/*")
    public Object oauth2Request() {
        System.out.println("------- 进入请求: " + SaHolder.getRequest().getUrl());

        return SaOAuth2Handle.serverRequest();
    }

    @Autowired
    @Transactional(rollbackFor = Exception.class)
    public void setSaOAuth2Config(SaOAuth2Config config) {
        config.
                setNotLoginView(() -> new ModelAndView("login.html"))
                .setDoLoginHandle((name, pwd) -> {
                    IUsersLoginVO vo = IUsersLoginVO.builder()
                            .username(name)
                            .password(pwd)
                            .build();
                    log.info(vo.toString());
                    ApiResult result = remoteUserService.doLogin(vo);
                    log.info(result.toString());
                    JSONObject userInfo = JSONUtil.parseObj(result.data).getJSONObject("userInfo");
                    IUsersEntity user = modelMapper.map(userInfo, IUsersEntity.class);
                    System.out.println("user:" + user);
                    if (result.code == HttpStatus.HTTP_OK) {
                        log.info("远程调用成功");
                        StpUtil.login(String.valueOf(user.getUid()));
                        log.info("user_id:" + user.getUid());
                        Optional<IOAuth2GrantEntity> optional =
                                auth2GrantEntityRepository.findByClientIdAndUserUid(1, user.getUid());
                        if (!optional.isPresent()) {
                            log.info("表中没有数据");
                            IOAuth2GrantEntity entity = new IOAuth2GrantEntity();
                            IAppClientsEntity appClient = appClientsEntityRepository.getOne(1);
                            entity.setClient(appClient);
                            usersEntityRepository.save(user);
                            entity.setUser(user);
                            entity.setOpenId(RandomUtil.randomString(32));
                            auth2GrantEntityRepository.save(entity);
                        } else {
                            IOAuth2GrantEntity entity = optional.get();
                            if (entity.getOpenId() == null) {
                                String openId = RandomUtil.randomString(32);
                                entity.setOpenId(openId);
                                auth2GrantEntityRepository.save(entity);
                            }
                        }
                        log.info("user_id:" + user.getUid());
                        return SaResult.ok();
                    } else {
                        return SaResult.error(result.msg);
                    }
                })
                .setConfirmView((clientId, scope) -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("clientId", clientId);
                    map.put("scope", scope);
                    return new ModelAndView("confirm.html", map);
                });
    }


    @ExceptionHandler
    public SaResult handleException(Exception e) {
        e.printStackTrace();
        return SaResult.error(e.getMessage());
    }

}
