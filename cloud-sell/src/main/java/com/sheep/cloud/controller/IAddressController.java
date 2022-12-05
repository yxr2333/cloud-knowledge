package com.sheep.cloud.controller;

import com.sheep.cloud.dto.request.sell.AddressParam;
import com.sheep.cloud.dto.request.sell.SaveAddressParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.IAddressControlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/12/5 星期一
 * Happy Every Coding Time~
 */

@RestController
@RequestMapping("/address")
@Api(tags = "收货地址管理")
public class IAddressController {

    @Autowired
    private IAddressControlService service;

    @ApiImplicitParam(name = "userId", value = "用户id", required = true)
    @ApiOperation(value = "查询某个用户的收货地址", notes = "查询某个用户的收货地址")
    @GetMapping("/user")
    public ApiResult<?> findAddressByUserId(@RequestParam Integer userId) {
        return service.findAddressByUserId(userId);
    }

    @ApiOperation(value = "更新地址信息", notes = "更新地址信息")
    @ApiImplicitParam(name = "addressParam", value = "地址信息", required = true, dataType = "AddressParam")
    @PutMapping
    public ApiResult<?> updateAddress(@RequestBody @Valid AddressParam addressParam) {
        return service.updateAddress(addressParam);
    }

    @PostMapping
    @ApiOperation(value = "新建地址信息", notes = "新建地址信息")
    @ApiImplicitParam(name = "param", value = "地址信息", required = true, dataType = "SaveAddressParam")
    public ApiResult<?> saveAddress(@RequestBody @Valid SaveAddressParam param) {
        return service.saveAddress(param);
    }

    @GetMapping("/find/{id}")
    @ApiOperation(value = "根据id查询地址信息", notes = "根据id查询地址信息")
    @ApiImplicitParam(name = "id", value = "地址id", required = true, dataType = "Integer")
    public ApiResult<?> findAddressById(@PathVariable Integer id) {
        return service.findAddressById(id);
    }

    @DeleteMapping
    @ApiOperation(value = "删除地址信息", notes = "删除地址信息")
    @ApiImplicitParam(name = "addressId", value = "地址id", required = true, dataType = "Integer")
    public ApiResult<?> deleteAddress(@RequestParam Integer addressId) {
        return service.deleteAddress(addressId);
    }
}
