package com.sheep.cloud.service;

import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.OAuth2TokenInfo;
import com.sheep.cloud.dto.response.knowledge.IUsersBaseInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service
 * @datetime 2022/9/22 星期四
 */
@FeignClient(value = "cloud-oauth2-service")
@RequestMapping("/oauth2")
public interface IRemoteOAuth2Service {


    @GetMapping("/userinfo")
    ApiResult<IUsersBaseInfoDTO> oauth2UserInfo(@RequestParam("access_token") String accessToken);

    @GetMapping("/token")
    ApiResult<OAuth2TokenInfo> oauth2Token(@RequestParam("grant_type") String grantType,
                                           @RequestParam("client_id") String clientId,
                                           @RequestParam("client_secret") String clientSecret,
                                           @RequestParam("code") String code);
}
