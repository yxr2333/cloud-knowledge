package com.sheep.cloud.controller;

import com.sheep.cloud.dto.request.sell.FindWishBuyConditionParam;
import com.sheep.cloud.dto.request.sell.PublishWishBuyEntityParam;
import com.sheep.cloud.dto.request.sell.UpdateWishBuyInfoParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.WishBuyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @ApiImplicitParam(name = "ids", value = "求购id的队列", required = true, dataType = "List<Integer>")
    @ApiOperation(value = "删除某条求购信息", notes = "删除某条求购信息")
    @DeleteMapping("/detail")
    public ApiResult<?> deleteMultiple(@RequestParam(required = false) List<Integer> ids) {
        return wishBuyService.deleteMultiple(ids);
    }

    @ApiImplicitParam(name = "param", value = "修改求购信息", required = true, dataType = "UpdateWishBuyInfoParam")
    @ApiOperation(value = "修改某条求购信息", notes = "修改某条求购信息")
    @PutMapping("/detail")
    public ApiResult<?> updateOne(@RequestBody @Valid UpdateWishBuyInfoParam param) {
        if (param.getId() == null) {
            return new ApiResult<>().fail("编号不能为空");
        }
        return wishBuyService.updateWishBuyDetail(param);
    }

    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "wishBuyConditionParam", value = "查询条件", dataType = "FindWishBuyConditionParam"),
                    @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer"),
                    @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10", dataType = "Integer")
            }
    )
    @ApiOperation(value = "分页获取所有商品", notes = "分页获取所有商品")
    @GetMapping("/list")
    public ApiResult<?> findAllWishBuyDetail(FindWishBuyConditionParam wishBuyConditionParam,
                                             @RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer size) {
        if (page == null || size == null) {
            page = 1;
            size = 10;
        }
        PageRequest pageable = PageRequest.of(page - 1, size);
        return wishBuyService.findWishBuyDetailConditionally(pageable,wishBuyConditionParam);
    }
}
