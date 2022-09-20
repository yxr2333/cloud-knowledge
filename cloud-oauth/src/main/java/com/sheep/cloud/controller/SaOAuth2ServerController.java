package com.sheep.cloud.controller;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Handle;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.http.HttpStatus;
import com.sheep.cloud.dao.IUsersEntityRepository;
import com.sheep.cloud.entity.IUsersEntity;
import com.sheep.cloud.request.IUsersLoginVO;
import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.response.IUsersBaseInfoDTO;
import com.sheep.cloud.service.RemoteUserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
                    IUsersEntity user = modelMapper.map(result.data, IUsersEntity.class);
                    if (result.code == HttpStatus.HTTP_OK) {
                        StpUtil.login(String.valueOf(user.getUid()));
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
