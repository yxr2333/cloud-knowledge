package com.sheep.cloud.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
public class SaveOneGoodParam {

    @NotNull(message = "未设置商品名称")
    @Length(min = 1, max = 30, message = "商品名称长度必须在1-20之间")
    private String goodsName;

    @NotNull(message = "未设置商品描述")
    @Length(min = 1, max = 100, message = "商品描述长度必须在1-100之间")
    private String description;

    @NotNull(message = "未设置商品价格")
    @Min(value = 0, message = "商品价格必须大于0")
    private Double price;

    @NotNull(message = "未设置商品品牌")
    private String brand;

    @NotNull(message = "未选择商品分类")
    private Integer typeId;

    @NotNull(message = "未设置商品库存")
    @Min(value = 0, message = "商品库存必须大于0")
    private Integer freeTotal;

    @NotNull(message = "未设置商品图片")
    private String cover;

    @NotNull(message = "未设置发布人编号")
    private Integer releaseUserId;

    @NotNull(message = "未设置是否折扣")
    private Boolean isDiscount;

    private Double discountPercent;
}
