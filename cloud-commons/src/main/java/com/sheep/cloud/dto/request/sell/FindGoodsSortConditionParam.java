package com.sheep.cloud.dto.request.sell;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Zhang Jinming
 * {@code @date} 6/12/2022 上午1:31
 */
@AllArgsConstructor
@Data
public class FindGoodsSortConditionParam {
    private boolean priceAsc;

    private boolean priceDesc;

    private boolean history;

    public FindGoodsSortConditionParam() {
        this.priceAsc = false;
        this.priceDesc = false;
        this.history = false;
    }
}
