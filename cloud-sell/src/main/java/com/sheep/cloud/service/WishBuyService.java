package com.sheep.cloud.service;

import com.sheep.cloud.dto.request.sell.PublishWishBuyEntityParam;
import com.sheep.cloud.dto.response.ApiResult;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service
 * @datetime 2022/10/10 星期一
 */
public interface WishBuyService {

    /**
     * 发布一条新的求购信息
     *
     * @param param 求购信息
     * @return 发布结果
     */
    ApiResult<?> publishOne(PublishWishBuyEntityParam param);

    /**
     * 根据id获取求购信息
     *
     * @param id 求购信息id
     * @return 求购信息
     */
    ApiResult<?> findWishBuyDetail(Integer id);

    /**
     * 根据id删除求购信息
     *
     * @param id 求购信息id
     * @return 删除结果
     */
    ApiResult<?> deleteOne(Integer id);
}
