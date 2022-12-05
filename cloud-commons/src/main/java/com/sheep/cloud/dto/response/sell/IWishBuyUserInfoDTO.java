package com.sheep.cloud.dto.response.sell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 5/12/2022 下午9:26
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IWishBuyUserInfoDTO {
    private Integer id;

    private IGoodsTypeEntityBaseInfoDTO type;

    private String description;

    private String imgUrl;

    private String pubTime;
}
