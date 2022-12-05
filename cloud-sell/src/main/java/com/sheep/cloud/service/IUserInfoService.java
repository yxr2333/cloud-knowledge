package com.sheep.cloud.service;

import com.sheep.cloud.dto.request.sell.UpdateUserEmailParam;
import com.sheep.cloud.dto.request.sell.UpdateUserInfoParam;
import com.sheep.cloud.dto.response.ApiResult;

/**
 * @author Zhang Jinming
 * @date 25/11/2022 下午2:33
 */
public interface IUserInfoService {
    ApiResult<?> updateEmail(UpdateUserEmailParam emailParam);

    ApiResult<?> updateBasicInfo(UpdateUserInfoParam infoParam);

    ApiResult<?> findUserInfoDetail(Integer id);

    ApiResult<?> findUserWishBuyList(Integer id);

    ApiResult<?> findUserOrderList(Integer id);

    ApiResult<?> findUserPublishGoodList(Integer id);

    ApiResult<?> findUserSellOrderList(Integer id);
}
