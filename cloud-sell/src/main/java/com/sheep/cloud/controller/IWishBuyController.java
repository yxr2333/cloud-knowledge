package com.sheep.cloud.controller;

import com.sheep.cloud.dto.request.sell.PublishWishBuyEntityParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.WishBuyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.controller
 * @datetime 2022/10/10 星期一
 */
@RestController
@RequestMapping("/wishBuy")
@Api(tags = "求购模块")
public class IWishBuyController {

    @Autowired
    private WishBuyService wishBuyService;

    @ApiImplicitParam(name = "param", value = "求购信息", required = true, dataType = "PublishWishBuyEntityParam")
    @ApiOperation(value = "发布一条求购", notes = "发布一条求购")
    @PostMapping("/publish")
    public ApiResult<?> publishOne(@RequestBody @Valid PublishWishBuyEntityParam param) {
        return wishBuyService.publishOne(param);
    }

    @ApiImplicitParam(name = "id", value = "求购id", required = true, dataType = "Integer")
    @ApiOperation(value = "获取某条求购信息", notes = "获取某条求购信息")
    @GetMapping("/detail")
    public ApiResult<?> findWishBuyDetail(@RequestParam(required = false) Integer id) {
        if (id == null) {
            return new ApiResult<>().fail("请选择编号");
        }
        return wishBuyService.findWishBuyDetail(id);
    }

}
