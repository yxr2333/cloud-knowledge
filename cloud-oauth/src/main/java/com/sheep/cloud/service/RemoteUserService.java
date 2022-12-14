package com.sheep.cloud.service;

import com.sheep.cloud.dto.request.knowledge.IUsersLoginVO;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.knowledge.IUserLoginResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service
 * @datetime 2022/9/15 星期四
 */
@FeignClient(value = "CLOUD-USER-SERVICE")
@Service
public interface RemoteUserService {

    @PostMapping("/user/login")
    ApiResult<IUserLoginResDTO> doLogin(@RequestBody @Valid IUsersLoginVO usersLoginVO);
}
