package com.sheep.cloud.service;

import com.sheep.cloud.dto.response.ApiResult;

public interface IShoppingCartService {

    /**
     * 创建一个购物车
     *
     * @id 用户id
     * @return 添加结果
     */
    ApiResult<?> creatShoppingCart(Integer id);

    /**
     * 删除购物车
     *
     * @id 购物车id
     * @return 添加结果
     */
    ApiResult<?> delShoppingCart(Integer id);

    /**
     * 添加商品
     *
     * @sid 购物车id
     * @gid 商品id
     * @return 添加结果
     */
    ApiResult<?> addOne(Integer sid, Integer gid);

    /**
     * 删除商品
     *
     * @sid 购物车id
     * @gid 商品id
     * @return 删除结果
     */
    ApiResult<?> deleteOne(Integer sid, Integer gid);

    /**
     * 查询购物车所有商品
     *
     * @param id   用户id
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 商品列表
     */
    ApiResult<?> getAll(Integer id,  Integer pageNum, Integer pageSize);

    /**
     * 清空购物车
     *
     * @id 购物车id
     * @return 清空结果
     */
    ApiResult<?> delAll(Integer id);
}
