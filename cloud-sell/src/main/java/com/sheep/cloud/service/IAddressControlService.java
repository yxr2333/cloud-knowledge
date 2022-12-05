package com.sheep.cloud.service;

import com.sheep.cloud.dto.request.sell.AddressParam;
import com.sheep.cloud.dto.request.sell.SaveAddressParam;
import com.sheep.cloud.dto.response.ApiResult;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/12/5 星期一
 * Happy Every Coding Time~
 */
public interface IAddressControlService {

    /**
     * 查询某个用户的收货地址
     *
     * @param userId 用户id
     * @return 收货地址列表
     */
    ApiResult<?> findAddressByUserId(Integer userId);

    /**
     * 更新地址信息
     *
     * @param addressParam 新的地址信息
     * @return 更新结果
     */
    ApiResult<?> updateAddress(AddressParam addressParam);

    /**
     * 新建地址信息
     *
     * @param saveAddressParam 地址信息
     * @return 新建结果
     */
    ApiResult<?> saveAddress(SaveAddressParam saveAddressParam);

    /**
     * 查询地址信息
     *
     * @param id 地址id
     * @return 地址信息
     */
    ApiResult<?> findAddressById(Integer id);

    ApiResult<?> deleteAddress(Integer addressId);

}
