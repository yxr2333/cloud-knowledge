package com.sheep.cloud.dto.request.knowledge;

import com.sheep.cloud.entity.knowledge.ILabelsEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class IResourceModifyVO implements Serializable {
    @NotNull(message = "资源编号不能为空")
    private Integer id;

    private String name;
    private String description;
    private String link;
    private String icon;
    private String content;
    private List<ILabelsEntity> labels;
}
