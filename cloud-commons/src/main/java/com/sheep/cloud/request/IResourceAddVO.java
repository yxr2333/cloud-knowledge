package com.sheep.cloud.request;


import com.sheep.cloud.entity.ILabelsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IResourceAddVO implements Serializable {
    @NotNull(message = "资源名称不能为空")
    private String name;

    @NotNull(message = "资源描述不能为空")
    private String description;

    @NotNull(message = "资源链接不能为空")
    private String link;

    private String icon;

    @NotNull(message = "请设置资源是否付费")
    private Boolean isPaid;
    private String password;
    @NotNull(message = "请先进行登录！")
    private Integer publishUser;

    @NotNull(message = "资源标签不能为空")
    private List<ILabelsEntity> labels;
}
