package com.sheep.cloud.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
public class UpdateGoodsInfoParam {


    @ApiModelProperty(value = "商品id", required = true)
    @NotNull(message = "未选择商品编号")
    private Integer id;

    @ApiModelProperty(value = "商品类别编号", required = true)
    @NotNull(message = "未选择商品类别")
    private Integer typeId;

    @ApiModelProperty(value = "商品名称", required = true)
    @NotBlank(message = "商品名称不能为空")
    private String name;

    @ApiModelProperty(value = "商品描述", required = true)
    @NotNull(message = "商品描述不能为空")
    private String description;

    @ApiModelProperty(value = "商品库存数", required = true)
    @Min(value = 0, message = "商品库存数不能小于0")
    private Integer freeTotal;

    @ApiModelProperty(value = "商品价格", required = true)
    @DecimalMin(value = "0.0", message = "商品价格不能小于0")
    private Double price;


    @ApiModelProperty(value = "商品是否打折", required = true)
    @NotNull(message = "未选定商品是否打折")
    private Boolean isDiscount;

    @ApiModelProperty(value = "商品折扣率", required = true)
    @DecimalMin(value = "0.0", message = "商品折扣率不能小于0")
    @DecimalMax(value = "1.0", message = "商品折扣率不能大于1")
    private Double discountPercent;

    @ApiModelProperty(value = "商品图片链接", required = true)
    @NotBlank(message = "商品图片不能为空")
    private String cover;
}
