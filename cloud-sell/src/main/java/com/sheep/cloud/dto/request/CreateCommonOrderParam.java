package com.sheep.cloud.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.dto.request
 * @datetime 2022/9/23 星期五
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommonOrderParam {

    @NotNull(message = "未选择所购买的商品编号")
    private Integer goodsId;

    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "商品价格必须大于0")
    private Double price;

    @NotNull(message = "未设置是否折扣")
    private Boolean isDiscount;

    private Double discountPercent;

    @NotNull(message = "未设置商家的编号")
    private Integer sellerId;

    @NotNull(message = "未设置商家的名称")
    private String sellerName;

    @NotNull(message = "未设置购买者的编号")
    private Integer buyerId;

    @NotNull(message = "未设置购买者的名称")
    private String buyerName;
}
