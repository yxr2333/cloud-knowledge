package com.sheep.cloud.service;

import com.sheep.cloud.dto.request.sell.UpdateLoginPasswordParam;
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

    /**
     * 更新用户的邮箱
     *
     * @param email  新的邮箱地址
     * @param userId 用户编号
     * @return 更新结果
     */
    ApiResult<?> updateSimpleEmail(String email, Integer userId);

    /**
     * 更新用户的登录密码
     *
     * @param param 更新参数
     * @return 更新结果
     */
    ApiResult<?> updateLoginPassword(UpdateLoginPasswordParam param);

    
    ApiResult<?> findUserWishBuyList(Integer id);

    ApiResult<?> findUserOrderList(Integer id);

    ApiResult<?> findUserPublishGoodList(Integer id);

    ApiResult<?> findUserSellOrderList(Integer id);
}
