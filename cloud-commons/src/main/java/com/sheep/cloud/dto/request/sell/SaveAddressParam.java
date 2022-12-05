package com.sheep.cloud.dto.request.sell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/12/5 星期一
 * Happy Every Coding Time~
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveAddressParam extends AddressParam {

    private Integer userId;

}
