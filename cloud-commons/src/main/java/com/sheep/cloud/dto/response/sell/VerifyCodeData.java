package com.sheep.cloud.dto.response.sell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.dto.response
 * @datetime 2022/9/22 星期四
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerifyCodeData implements Serializable {

    private String verifyCode;
    private String requestKey;
}
