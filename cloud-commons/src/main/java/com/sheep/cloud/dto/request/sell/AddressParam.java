package com.sheep.cloud.dto.request.sell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class AddressParam {

    private Integer id;

    @NotNull(message = "地址不能为空")
    @NotBlank(message = "地址不能为空")
    private String address;

    @NotNull(message = "收货人不能为空")
    @NotBlank(message = "收货人不能为空")
    private String name;

    @NotNull(message = "电话不能为空")
    @NotBlank(message = "电话不能为空")
    private String phone;
}
