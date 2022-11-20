package com.sheep.cloud.dto.request.sell;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PublishWishBuyEntityParam implements Serializable {

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