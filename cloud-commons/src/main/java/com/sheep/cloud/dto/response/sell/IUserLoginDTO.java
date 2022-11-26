package com.sheep.cloud.dto.response.sell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/11/23 星期三
 * Happy Every Coding Time~
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IUserLoginDTO implements Serializable {

    private static final long serialVersionUID = 3093661248279046154L;

    private TokenInfo tokenInfo;
    private IUserEntityBaseInfo baseInfo;
}
