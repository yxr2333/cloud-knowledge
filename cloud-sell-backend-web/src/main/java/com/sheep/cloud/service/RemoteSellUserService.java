package com.sheep.cloud.service;

import com.sheep.cloud.dto.request.knowledge.IUsersLoginVO;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.sell.IUserEntityBaseInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/11/24 星期四
 * Happy Every Coding Time~
 */

@FeignClient(value = "cloud-sell-service", contextId = "cloud-sell-service")
@RequestMapping("/sell/user")
public interface RemoteSellUserService {

    @PostMapping("/admin/login")
    ApiResult<IUserEntityBaseInfo> doAdminLogin(@RequestBody @Valid IUsersLoginVO param);
}
