package com.sheep.cloud.dto.request.knowledge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.request
 * @datetime 2022/8/12 星期五
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IUsersLoginVO implements Serializable {
    private String username;
    private String password;
}
