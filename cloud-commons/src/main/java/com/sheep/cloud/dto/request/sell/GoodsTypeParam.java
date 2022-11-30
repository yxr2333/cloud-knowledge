package com.sheep.cloud.dto.request.sell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsTypeParam {
    @NotNull(message = "标签名称不允许为空")
    @Length(min = 1, max = 30, message = "标签名称长度必须在1-20之间")
    private String name;

    //父级id
    private Integer fid;
}
