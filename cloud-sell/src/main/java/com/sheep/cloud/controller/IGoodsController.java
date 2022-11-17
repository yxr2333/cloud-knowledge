package com.sheep.cloud.controller;

import com.sheep.cloud.dto.request.SaveOneGoodParam;
import com.sheep.cloud.dto.request.UpdateGoodsInfoParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.IGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

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


    @ApiImplicitParam(name = "id", value = "商品id", required = true, dataType = "Integer")
    @ApiOperation(value = "软删除某个商品", notes = "软删除某个商品")
    @DeleteMapping("/del")
    public ApiResult logicDeleteOne(@RequestParam(required = false) Integer goodsId) {
        if (goodsId == null) {
            return ApiResult.fail("商品id不能为空");
        }
        return goodsService.deleteOne(goodsId);
    }

    @ApiImplicitParam(name = "gid", value = "商品id", required = true, dataType = "Integer")
    @ApiOperation(value = "根据id获取商品信息", notes = "根据id获取商品信息")
    @GetMapping("/detail")
    public ApiResult findGoodsDetail(@RequestParam(required = false) Integer gid) {
        if (gid == null) {
            return ApiResult.fail();
        }
        return goodsService.findGoodsDetail(gid);
    }


    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer"),
                    @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10", dataType = "Integer")
            }
    )
    @ApiOperation(value = "分页获取所有商品", notes = "分页获取所有商品")
    @GetMapping("/list")
    public ApiResult findAllGoods(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page == null || size == null) {
            page = 1;
            size = 10;
        }
        PageRequest pageable = PageRequest.of(page - 1, size);
        return goodsService.findAllGoods(pageable);
    }

    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer"),
                    @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10", dataType = "Integer"),
                    @ApiImplicitParam(name = "uid", value = "发布人", required = true, dataType = "Integer")
            }
    )
    @ApiOperation(value = "分页获取某个用户发布的所有商品", notes = "分页获取某个用户发布的所有商品")
    @GetMapping("/list/uid")
    public ApiResult findAllGoodsByUserId(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Integer uid) {
        if (uid == null) return ApiResult.fail("用户id不能为空");
        if (page == null || size == null) {
            page = 1;
            size = 10;
        }
        PageRequest pageable = PageRequest.of(page - 1, size);
        return goodsService.findAllGoodsByUserId(pageable, uid);
    }


    @ApiImplicitParam(name = "param", value = "商品信息", required = true, dataType = "UpdateGoodsInfoParam")
    @ApiOperation(value = "更新商品信息", notes = "更新商品信息")
    @PutMapping
    public ApiResult updateGoodsInfo(@RequestBody @Valid UpdateGoodsInfoParam param) {
        return goodsService.updateGoodsInfo(param);
    }


}
