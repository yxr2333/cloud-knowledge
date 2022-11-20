package com.sheep.cloud.dto.response.knowledge;

import com.sheep.cloud.entity.knowledge.ILabelsEntity;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.response
 * @datetime 2022/8/13 星期六
 */
@Data
@NoArgsConstructor
public class IUsersBaseInfoDTO implements Serializable {
    private Integer uid;
    private String username;
    private String gender;
    private String email;
    private String avatar;
    private Integer score;
    private String description;
    private List<ILabelsEntity> labels;
}
