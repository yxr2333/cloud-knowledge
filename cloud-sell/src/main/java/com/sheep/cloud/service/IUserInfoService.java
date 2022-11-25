package com.sheep.cloud.service;

import com.sheep.cloud.dto.response.ApiResult;

/**
 * @author Zhang Jinming
 * @date 25/11/2022 下午2:33
 */
public interface IUserInfoService {
    ApiResult<?> publishOne();

    ApiResult<?> updateOne();

    ApiResult<?> findUserInfoDetail(Integer id);
}
