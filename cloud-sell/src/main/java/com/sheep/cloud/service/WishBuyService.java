package com.sheep.cloud.service;

import com.sheep.cloud.dto.request.sell.FindWishBuyConditionParam;
import com.sheep.cloud.dto.request.sell.PublishWishBuyEntityParam;
import com.sheep.cloud.dto.request.sell.UpdateWishBuyInfoParam;
import com.sheep.cloud.dto.response.ApiResult;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service
 * @datetime 2022/10/10 星期一
 */
public interface WishBuyService {

    /**
     * 发布一条新地求购信息
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
     * 根据id删除多条求购信息
     *
     * @param ids 求购信息id的队列
     * @return 删除结果
     */
    ApiResult<?> deleteMultiple(List<Integer> ids);

    /**
     * 根据id更新求购信息
     *
     * @param param 修改求购信息
     * @return 修改结果
     */
    ApiResult<?> updateWishBuyDetail(UpdateWishBuyInfoParam param);

    ApiResult<?> findWishBuyDetailConditionally(Pageable pageable, FindWishBuyConditionParam wishBuyConditionParam);
}
