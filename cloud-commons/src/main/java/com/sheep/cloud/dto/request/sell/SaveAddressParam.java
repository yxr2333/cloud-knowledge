package com.sheep.cloud.dto.request.sell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/12/5 ζζδΈ
 * Happy Every Coding Time~
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveAddressParam extends AddressParam {

    private Integer userId;

}
