package com.sheep.cloud.controller;


import com.sheep.cloud.dto.request.sell.GoodsTypeInfoParam;
import com.sheep.cloud.dto.request.sell.GoodsTypeParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.IGoodsTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created By Intellij IDEA
 *
 * @author WTY2002
 * @package com.sheep.cloud.controller
 * @datetime 2022/11/30
 */

@RestController
@RequestMapping("/goodsType")
@Api(tags = "商品类别模块")
public class IGoodsTypeComtroller {
    @Autowired
    IGoodsTypeService iGoodsTypeService;


    /**
     * 添加商品类别
     *
     * @param goodsTypeParam 商品标签信息
     * @return 添加结果
     */
    @ApiImplicitParam(name = "goodsTypeParam", value = "商品类别信息", required = true, dataType = "GoodsTypeParam")
    @ApiOperation(value = "添加一个商品类别", notes = "添加一个商品类别")
    @PostMapping("/add")
    public ApiResult<?> addIGoodsType(@RequestBody @Valid GoodsTypeParam goodsTypeParam) {
        return iGoodsTypeService.addIGoodsType(goodsTypeParam);
    }

    /**
     * 删除商品类别
     *
     * @param id 商品标签id
     * @return 删除结果
     */
    @ApiImplicitParam(name = "id", value = "商品类别id", required = true, dataType = "Integer")
    @ApiOperation(value = "删除某个商品类别", notes = "删除某个商品类别")
    @DeleteMapping("/del")
    public ApiResult<?> delIGoodsType(@RequestParam(required = false) Integer id) {
        return iGoodsTypeService.delIGoodsType(id);
    }

    /**
     * 修改商品类别
     *
     * @param goodsTypeInfoParam 商品标签信息
     * @return 修改结果
     */
    @ApiImplicitParam(name = "goodsTypeInfoParam", value = "商品类别信息", required = true, dataType = "GoodsTypeInfoParam")
    @ApiOperation(value = "修改商品类别信息", notes = "修改商品类别信息")
    @PutMapping
    public ApiResult<?> modifyIGoodsType(@RequestBody @Valid GoodsTypeInfoParam goodsTypeInfoParam) {
        return iGoodsTypeService.modifyIGoodsType(goodsTypeInfoParam);
    }

    /**
     * 分页查询所有商品类别
     *
     * @return 查询结果
     */
    @ApiOperation(value = "获取所有商品类别", notes = "获取所有商品类别")
    @GetMapping("/getAll")
    public ApiResult<?> getAllGoodsType() {
        return iGoodsTypeService.getAllIGoodsType();
    }

    /**
     * 查询单个商品类别
     *
     * @param id 商品标签id
     * @return 查询结果
     */
    @ApiOperation(value = "查询指定商品类别", notes = "查询指定商品类别")
    @GetMapping("/getOne")
    public ApiResult<?> getOneIGoodsType(@RequestParam(required = false) Integer id) {
        return iGoodsTypeService.getOneIGoodsType(id);
    }

}
