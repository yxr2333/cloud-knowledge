package com.sheep.cloud.service;

import com.sheep.cloud.dto.request.sell.GoodsTypeInfoParam;
import com.sheep.cloud.dto.request.sell.GoodsTypeParam;
import com.sheep.cloud.dto.response.ApiResult;
import org.springframework.data.domain.Pageable;

public interface IGoodsTypeService {
    /**
     * 添加商品类别
     *
     * @param goodsTypeParam  商品标签信息
     * @return 添加结果
     */
    ApiResult<?> addIGoodsType(GoodsTypeParam goodsTypeParam);

    /**
     * 删除商品类别
     *
     * @param id  商品标签id
     * @return 删除结果
     */
    ApiResult<?> delIGoodsType(Integer id);

    /**
     * 修改商品类别
     *
     * @param goodsTypeInfoParam 商品标签信息
     * @return 修改结果
     */
    ApiResult<?> modifyIGoodsType(GoodsTypeInfoParam goodsTypeInfoParam);

    /**
     * 分页查询所有商品类别
     *
     * @param pageable 分页信息
     * @return 查询结果
     */
    ApiResult<?> getAllIGoodsType(Pageable pageable);

    /**
     * 查询单个商品类别
     *
     * @param id  商品标签id
     * @return 查询结果
     */
    ApiResult<?> getOneIGoodsType(Integer id);
}
