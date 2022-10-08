package com.sheep.cloud.common;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.common
 * @datetime 2022/10/8 星期六
 */
public enum SafeAccountHistoryEnum {
    /**
     * TRANS_IN 转入
     * TRANS_OUT 转出
     */
    TRANS_IN(1, "买家支付订单，钱转入安全账户暂存"),
    TRANS_OUT(2, "转出"),

    TRANS_OUT_TO_SELLER(3, "买家确认收货，钱转给卖家"),

    TRANS_OUT_TO_BUYER(4, "买家取消订单或退款，钱转给买家");
    public Integer type;
    public String reason;

    SafeAccountHistoryEnum() {
    }

    SafeAccountHistoryEnum(Integer type, String reason) {
        this.type = type;
        this.reason = reason;
    }
}
