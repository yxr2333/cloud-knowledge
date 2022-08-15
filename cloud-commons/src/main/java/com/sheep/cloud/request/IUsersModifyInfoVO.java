package com.sheep.cloud.request;

import com.sheep.cloud.entity.ILabelsEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.request
 * @datetime 2022/8/12 星期五
 */
@Data
@NoArgsConstructor
public class IUsersModifyInfoVO implements Serializable {


    @NotNull(message = "用户编号不能为空")
    private Integer id;

    private String username;
    private String avatar;
    private String description;
    private String email;
    private List<ILabelsEntity> labels;
}
