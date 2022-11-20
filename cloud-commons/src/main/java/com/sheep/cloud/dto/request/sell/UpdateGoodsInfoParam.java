package com.sheep.cloud.dto.request.sell;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/11/2 星期三
 * Happy Every Coding Time~
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGoodsInfoParam {


    @NotNull(message = "未选择商品编号")
    private Integer id;

    @NotNull(message = "未选择商品类别")
    private Integer typeId;

    @NotBlank(message = "商品名称不能为空")
    private String name;

    @NotNull(message = "商品描述不能为空")
    private String description;

    @Min(value = 0, message = "商品库存数不能小于0")
    private Integer freeTotal;

    @DecimalMin(value = "0.0", message = "商品价格不能小于0")
    private Double price;


    @NotNull(message = "未选定商品是否打折")
    private Boolean isDiscount;

    @DecimalMin(value = "0.0", message = "商品折扣率不能小于0")
    @DecimalMax(value = "1.0", message = "商品折扣率不能大于1")
    private Double discountPercent;

    @NotBlank(message = "商品图片不能为空")
    private String cover;
}
