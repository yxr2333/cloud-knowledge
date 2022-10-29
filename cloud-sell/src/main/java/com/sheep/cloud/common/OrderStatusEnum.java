package com.sheep.cloud.common;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.common
 * @datetime 2022/9/23 星期五
 */
public enum OrderStatusEnum {

    /**
     * NOT_PAYED "未支付"
     * PAYED_NOT_CHECKED 已支付，未审核
     * CHECKED_NOT_DELIVERED 已审核，未发货
     * DELIVERED_NOT_RECEIVED 已发货，未收货
     * FINISHED 已收货，完成
     * CANCELED "已取消"
     * REFUNDED "已退款"
     */
    NOT_PAYED(0, "未支付"),
    PAYED_NOT_CHECKED(1, "已支付，未审核"),
    CHECKED_NOT_DELIVERED(2, "已审核，未发货"),
    DELIVERED_NOT_RECEIVED(3, "已发货，未收货"),
    FINISHED(4, "已收货，完成"),
    CANCELED(5, "已取消"),
    REFUNDED(6, "已退款"),

    APPLY_NOT_REFUNDED(7, "已申请退款，等待卖家处理"),

    REFUSED_REFUND(8, "卖家拒绝退款"),

    GOODS_DOWN_CANCELED(9, "商品下架，订单取消");

    public Integer code;
    public String description;

    OrderStatusEnum() {
    }

    OrderStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
