package com.sheep.cloud.dto.request.sell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 25/11/2022 下午4:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserInfoParam {

    private Integer id;

    private String username;

    private String description;

    private String avatar;

}
