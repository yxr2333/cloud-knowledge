package com.sheep.cloud.controller;

import com.sheep.cloud.dto.request.sell.CreateSpikeParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.ISpikeService;
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

@RequestMapping("/spike")
@RestController
@Api(tags = "秒杀活动")
public class ISpikeController {

    @Autowired
    private ISpikeService service;
    
    @ApiOperation(value = "创建秒杀活动", notes = "创建秒杀活动")
    @ApiImplicitParam(name = "vo", value = "秒杀活动信息", required = true, dataType = "CreateSpikeParam")
    @PostMapping
    public ApiResult<?> createOne(@RequestBody @Valid CreateSpikeParam vo) {
        return service.createOne(vo);
    }

    @ApiOperation(value = "获取最近的秒杀活动", notes = "获取最近的秒杀活动")
    @ApiImplicitParam(name = "size", value = "活动条数", required = true, dataType = "Integer")
    @GetMapping("/recent")
    public ApiResult<?> showRecentSpike(@RequestParam Integer size) {
        return service.showRecentSpike(size);
    }

    @ApiOperation(value = "获取秒杀活动详情", notes = "获取秒杀活动详情")
    @ApiImplicitParam(name = "spikeId", value = "秒杀活动id", required = true, dataType = "Integer")
    @GetMapping("/details")
    public ApiResult<?> findSpikeDetails(@RequestParam Integer spikeId) {
        return service.findSpikeDetails(spikeId);
    }
}
