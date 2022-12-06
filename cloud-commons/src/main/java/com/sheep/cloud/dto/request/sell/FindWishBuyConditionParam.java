package com.sheep.cloud.dto.request.sell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 1/12/2022 下午5:33
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FindWishBuyConditionParam {

    private String goodName;

    private String type;

    private String description;

    private String beginTime;

    private String endTime;
}
