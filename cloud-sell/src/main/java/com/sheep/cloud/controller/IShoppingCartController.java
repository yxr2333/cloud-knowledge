package com.sheep.cloud.controller;

import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.IShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created By Intellij IDEA
 *
 * @author WTY2002
 * @package com.sheep.cloud.controller
 * @datetime 2022/11/26
 */

@RestController
@RequestMapping("/cart")
@Api(tags = "购物车模块")
public class IShoppingCartController {
    @Autowired
    private IShoppingCartService iShoppingCartService;


    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Integer")
    @ApiOperation(value = "创建一个购物车", notes = "创建一个购物车")
    @PostMapping("/create")
    public ApiResult<?> creatOne(@RequestParam(required = false) Integer id) {
        return iShoppingCartService.creatShoppingCart(id);
    }

    @ApiImplicitParam(name = "id", value = "购物车id", required = true, dataType = "Integer")
    @ApiOperation(value = "删除购物车", notes = "删除购物车")
    @DeleteMapping("/delcart")
    public ApiResult<?> deleteOneCart(@RequestParam(required = false) Integer id) {
        if (id == null) {
            return new ApiResult<>().fail("购物车id不能为空！");
        }
        return iShoppingCartService.delShoppingCart(id);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "购物车id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "gid", value = "商品id", required = true, dataType = "Integer")
    })
    @ApiOperation(value = "添加一个商品", notes = "添加一个商品")
    @PutMapping("/add")
    public ApiResult<?> saveOne(@RequestParam(required = false) Integer sid, @RequestParam(required = false) Integer gid) {
        return iShoppingCartService.addOne(sid,gid);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "购物车id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "gid", value = "商品id", required = true, dataType = "Integer")
    })
    @ApiOperation(value = "移除一个商品", notes = "移除一个商品")
    @PutMapping("/del")
    public ApiResult<?> delOne(@RequestParam(required = false) Integer sid, @RequestParam(required = false) Integer gid) {
        return iShoppingCartService.deleteOne(sid,gid);
    }

    /**
     * 查询购物车所有商品
     *
     * @param id   用户id
     * @param page 页号
     * @param size 页面数据条数
     * @return 商品列表
     */
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Integer")
    @ApiOperation(value = "查看用户的购物车", notes = "查看用户的购物车")
    @GetMapping("/shopping")
    public ApiResult<?> findAllGoodsByUserId(@RequestParam(value = "id", required = false) Integer id,
                                             @RequestParam(value = "page", required = false) Integer page,
                                             @RequestParam(value = "size", required = false) Integer size) {
        if (id == null) return new ApiResult<>().fail("用户id不能为空");
        if (page == null || size == null) {
            page = 3;
            size = 0;
        }
        Integer limit = size * (page > 0 ? 1 : 0);
        Integer offset = page > 0 ? (page - 1) * size : 0;
        return iShoppingCartService.getAll(id,limit,offset);
    }

    @ApiImplicitParam(name = "id", value = "购物车id", required = true, dataType = "Integer")
    @ApiOperation(value = "清空购物车", notes = "清空购物车")
    @PutMapping("/delAll")
    public ApiResult<?> delAll(@RequestParam(required = false) Integer id) {
        return iShoppingCartService.delAll(id);
    }
}
