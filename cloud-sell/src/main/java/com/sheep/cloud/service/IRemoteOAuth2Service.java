package com.sheep.cloud.service;

import com.sheep.cloud.dto.response.ApiResult;
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
@FeignClient(value = "CLOUD-OAUTH2-SERVICE")
@RequestMapping("/oauth2")
public interface IRemoteOAuth2Service {


    @GetMapping("/userinfo")
    ApiResult oauth2UserInfo(@RequestParam("access_token") String accessToken);
}
