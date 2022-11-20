package com.sheep.cloud.dto.response.sell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.dto.response
 * @datetime 2022/9/20 星期二
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo {

    @JsonProperty("name")
    private String tokenName;

    private String token;
}
