package com.sheep.cloud.service;

import com.sheep.cloud.dto.request.sell.*;
import com.sheep.cloud.dto.response.ApiResult;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service
 * @datetime 2022/9/23 星期五
 */
public interface IOrderService {

    /**
     * 创建一个普通交易的订单
     *
     * @param param 订单信息
     * @return 创建结果
     */
    ApiResult<?> createCommonOrder(CreateCommonOrderParam param);

    /**
     * 支付订单
     *
     * @param param 订单信息
     * @return 支付结果
     */
    ApiResult<?> payOrder(PayOrderParam param);

    /**
     * 取消订单
     *
     * @param param 订单信息
     * @return 取消结果
     */
    ApiResult<?> cancelOrder(CancelOrderParam param);

    /**
     * 审核订单是否支付成功
     *
     * @param orderId 订单id
     * @return 审核结果
     */
    ApiResult<?> checkPay(String orderId);

    /**
     * 卖家发货
     *
     * @param orderId 订单id
     * @return 发货结果
     */
    ApiResult<?> deliverOrder(String orderId);

    /**
     * 买家确认收货
     *
     * @param orderId 订单id
     * @return 确认收货结果
     */
    ApiResult<?> checkSaveGoods(String orderId);

    /**
     * 申请退款
     *
     * @param param 申请退款信息
     * @return 申请退款结果
     */
    ApiResult<?> applyForRefund(ReplyRefundOrderParam param);

    /**
     * 卖家审核退款
     *
     * @param param 审核退款信息
     * @return 审核退款结果
     */
    ApiResult<?> checkRefund(CheckRefundOrderParam param);

}
