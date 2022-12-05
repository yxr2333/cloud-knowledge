package com.sheep.cloud.controller;

import com.sheep.cloud.dto.request.sell.UpdateLoginPasswordParam;
import com.sheep.cloud.dto.request.sell.UpdateUserEmailParam;
import com.sheep.cloud.dto.request.sell.UpdateUserInfoParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.IUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Zhang Jinming
 * @date 26/11/2022 下午7:25
 */
@RequestMapping("/userinfo")
@RestController
@Api(tags = "用户信息模块")
public class IUserInfoController {
    @Autowired
    private IUserInfoService userInfoService;

    @ApiImplicitParam(name = "id", value = "用户的详细信息", required = true, dataType = "Integer")
    @ApiOperation(value = "查询用户的详细信息", notes = "查询用户的详细信息")
    @GetMapping("/basic")
    public ApiResult<?> findUserInfoDetail(@RequestParam Integer id) {
        if (id == null) {
            return new ApiResult<>().error("id不能为空");
        }
        return userInfoService.findUserInfoDetail(id);
    }

    @ApiImplicitParam(name = "vo", value = "用户的基础信息", required = true, dataType = "UpdateUserInfoParam")
    @ApiOperation(value = "更新用户的基础信息", notes = "更新用户的基础信息")
    @PutMapping("/basic")
    public ApiResult<?> updateBasicInfo(@RequestBody @Valid UpdateUserInfoParam vo) {
        return userInfoService.updateBasicInfo(vo);
    }

    @ApiImplicitParam(name = "vo", value = "用户的邮箱验证表单", required = true, dataType = "UpdateUserEmailParam")
    @ApiOperation(value = "更新用户的邮箱", notes = "更新用户的邮箱")
    @PutMapping("/email")
    public ApiResult<?> updateUserEmail(@RequestBody @Valid UpdateUserEmailParam vo) {
        return userInfoService.updateEmail(vo);
    }

    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "email", value = "用户的邮箱", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "userId", value = "用户的编号", required = true, dataType = "Integer")
            }
    )
    @ApiOperation(value = "更新用户的邮箱", notes = "更新用户的邮箱")
    @PutMapping("/email/new")
    public ApiResult<?> updateUserEmail(@RequestParam String email, @RequestParam Integer userId) {
        return userInfoService.updateSimpleEmail(email, userId);
    }

    @ApiImplicitParam(name = "vo", value = "用户的登录密码表单", required = true, dataType = "UpdateLoginPasswordParam")
    @ApiOperation(value = "更新用户的登录密码", notes = "更新用户的登录密码")
    @PutMapping("/password")
    public ApiResult<?> updateLoginPassword(@RequestBody @Valid UpdateLoginPasswordParam param) {
        return userInfoService.updateLoginPassword(param);
    }
}
