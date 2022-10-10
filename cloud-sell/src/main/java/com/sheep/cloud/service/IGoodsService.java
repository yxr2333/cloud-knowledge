package com.sheep.cloud.service;

import com.sheep.cloud.dto.request.SaveOneGoodParam;
import com.sheep.cloud.dto.response.ApiResult;
import org.springframework.data.domain.Pageable;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service
 * @datetime 2022/9/16 星期五
 */
public interface IGoodsService {


    /**
     * 发布一个商品
     *
     * @param param 商品信息
     * @return 发布结果
     */
    ApiResult saveOne(SaveOneGoodParam param);

    /**
     * 软删除某个商品
     *
     * @param id 商品id
     * @return 删除结果
     */
    ApiResult deleteOne(Integer id);

    /**
     * 获取某个商品详情
     *
     * @param goodsId 商品id
     * @return 商品详情
     */
    ApiResult findGoodsDetail(Integer goodsId);

    /**
     * 分页获取所有商品
     *
     * @param pageable 分页信息
     * @return 商品列表
     */
    ApiResult findAllGoods(Pageable pageable);

    /**
     * 分页获取某个用户发布的商品
     *
     * @param pageable 分页信息
     * @param userId   用户id
     * @return 商品列表
     */
    ApiResult findAllGoodsByUserId(Pageable pageable, Integer userId);
}
