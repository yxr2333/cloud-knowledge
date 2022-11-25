package com.sheep.cloud.dto.request.sell;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Zhang Jinming
 * @date 24/11/2022 下午8:11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWishBuyInfoParam {
    @NotNull(message = "未指定商品的id")
    private Integer id;

    @NotNull(message = "未选定求购的类型")
    private Integer typeId;

    @NotNull(message = "求购描述不能为空")
    @NotBlank(message = "求购描述不能为空")
    private String description;

    @NotNull(message = "未上传求购图片")
    private String imgUrl;

    @NotNull(message = "未指定求购者")
    private Integer pubUserId;
}
