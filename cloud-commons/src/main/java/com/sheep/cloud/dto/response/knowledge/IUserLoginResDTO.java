package com.sheep.cloud.dto.response.knowledge;

import com.sheep.cloud.entity.knowledge.IUsersEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/11/18 星期五
 * Happy Every Coding Time~
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IUserLoginResDTO {
    private String token;
    private String tokenName;
    private IUsersEntity user;
}
