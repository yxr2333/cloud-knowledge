package com.sheep.cloud.service;

import com.sheep.cloud.dto.request.sell.CreateSpikeParam;
import com.sheep.cloud.dto.response.ApiResult;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/12/5 星期一
 * Happy Every Coding Time~
 */
public interface ISpikeService {

    /**
     * 创建一个秒杀活动
     *
     * @param vo 秒杀活动信息
     * @return 创建结果
     */
    ApiResult<?> createOne(CreateSpikeParam vo);

    /**
     * 展示最近的size条秒杀活动
     *
     * @param size 展示数量
     * @return 秒杀活动列表
     */
    ApiResult<?> showRecentSpike(Integer size);


    /**
     * 查找某个秒杀活动的详情
     *
     * @param spikeId 秒杀活动id
     * @return 秒杀活动详情
     */
    ApiResult<?> findSpikeDetails(Integer spikeId);


}
