package com.sheep.cloud.controller;

import com.sheep.cloud.dto.request.CancelOrderParam;
import com.sheep.cloud.dto.request.CreateCommonOrderParam;
import com.sheep.cloud.dto.request.PayOrderParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.IOrderService;
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
 * @datetime 2022/9/23 星期五
 */
@RestController
@RequestMapping("/order")
@Api(tags = "订单模块")
public class IOrderController {

    @Autowired
    private IOrderService orderService;


    @ApiImplicitParam(name = "param", value = "订单信息", required = true, dataType = "CreateCommonOrderParam")
    @ApiOperation(value = "创建一个普通交易的订单", notes = "创建一个普通交易的订单")
    @PostMapping("/create/common")
    public ApiResult createOrder(@RequestBody @Valid CreateCommonOrderParam param) {
        return orderService.createCommonOrder(param);
    }

    @ApiImplicitParam(name = "param", value = "待支付的订单信息", required = true, dataType = "PayOrderParam")
    @ApiOperation(value = "支付订单", notes = "支付订单")
    @PostMapping("/pay")
    public ApiResult payOrder(@RequestBody @Valid PayOrderParam param) {
        return orderService.payOrder(param);
    }

    @ApiImplicitParam(name = "param", value = "待取消的订单信息", required = true, dataType = "CancelOrderParam")
    @ApiOperation(value = "取消订单", notes = "取消订单")
    @PostMapping("/cancel")
    public ApiResult cancelOrder(@RequestBody @Valid CancelOrderParam param) {
        return orderService.cancelOrder(param);
    }

    @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataType = "String")
    @ApiOperation(value = "审核订单是否支付成功", notes = "审核订单是否支付成功")
    @PostMapping("/check/pay")
    public ApiResult checkPay(@RequestParam(required = false) String orderId) {
        if (orderId == null) {
            return ApiResult.error("订单编号不能为空");
        }
        return orderService.checkPay(orderId);
    }

    @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataType = "String")
    @ApiOperation(value = "卖家发货", notes = "卖家发货")
    @PostMapping("/deliver/goods")
    public ApiResult deliverOrder(@RequestParam(required = false) String orderId) {
        if (orderId == null) {
            return ApiResult.error("订单编号不能为空");
        }
        return orderService.deliverOrder(orderId);
    }

    @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataType = "String")
    @ApiOperation(value = "买家确认收货", notes = "买家确认收货")
    @PostMapping("/confirm/save")
    public ApiResult checkSaveGoods(@RequestParam(required = false) String orderId) {
        if (orderId == null) {
            return ApiResult.error("订单编号不能为空");
        }
        return orderService.checkSaveGoods(orderId);
    }
}
