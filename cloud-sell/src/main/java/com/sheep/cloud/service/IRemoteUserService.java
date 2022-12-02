package com.sheep.cloud.service;


import com.sheep.cloud.dto.request.knowledge.IUsersRegisterVO;
import com.sheep.cloud.dto.response.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service
 * @datetime 2022/9/16 星期五
 */
@FeignClient(value = "cloud-user-service", contextId = "cloud-user-service")
public interface IRemoteUserService {

    @PostMapping("/user/register/remote")
    ApiResult<IUsersRegisterVO> doRemoteRegister(@RequestBody @Valid IUsersRegisterVO vo);

    @PostMapping(value = "/common/upload", consumes = "multipart/form-data", produces = "application/json")
    ApiResult<?> uploadImg(@RequestPart("file") MultipartFile file) throws IOException;

    @PostMapping(value = "/os/default", consumes = "multipart/form-data", produces = "application/json")
    ApiResult<?> uploadImgNew(
            @RequestPart("file") MultipartFile file);
}
