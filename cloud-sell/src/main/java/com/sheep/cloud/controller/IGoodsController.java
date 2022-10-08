package com.sheep.cloud.controller;

import com.sheep.cloud.dto.request.SaveOneGoodParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.IGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.controller
 * @datetime 2022/9/23 星期五
 */
@RestController
@RequestMapping("/goods")
@Api(tags = "商品模块")
public class IGoodsController {

    @Autowired
    private IGoodsService goodsService;

    @ApiImplicitParam(name = "param", value = "商品信息", required = true, dataType = "SaveOneGoodParam")
    @ApiOperation(value = "发布一个商品", notes = "发布一个商品")
    @PostMapping("/save")
    public ApiResult saveOne(@RequestBody @Valid SaveOneGoodParam param) {
        return goodsService.saveOne(param);
    }
}
