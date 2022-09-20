package com.sheep.cloud.service;

import com.sheep.cloud.dto.response.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service
 * @datetime 2022/9/20 星期二
 */
@FeignClient(value = "CLOUD-USER-SERVICE")
@RequestMapping("/mail")
public interface IRemoteMailService {

    @PostMapping("/sendResetCode")
    ApiResult sendResetCode(HttpServletRequest request, String email);
}
