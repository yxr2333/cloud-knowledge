package com.sheep.cloud.service;


import com.sheep.cloud.dto.request.IUsersRegisterVO;
import com.sheep.cloud.dto.response.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service
 * @datetime 2022/9/16 星期五
 */
@FeignClient(value = "CLOUD-USER-SERVICE")
@RequestMapping("/user")
public interface IRemoteUserService {

    @PostMapping("/register/remote")
    ApiResult doRemoteRegister(@RequestBody @Valid IUsersRegisterVO vo);
}
