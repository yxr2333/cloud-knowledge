package com.sheep.cloud.service;

import com.sheep.cloud.entity.IResourcesEntity;
import com.sheep.cloud.response.ApiResult;

public interface ResourceService {
    /**
     * 发布资源
     *
     * @param iResourcesEntity 资源
     * @return 添加结果
     */
    ApiResult addOne(IResourcesEntity iResourcesEntity);

    /**
     * 通过用户名id删除资源
     *
     * @param id 资源id
     * @return 删除结果
     */
    ApiResult deleteResourceById(Integer id);

    /**
     * 用户修改信息
     *
     * @param iResourcesEntity 修改资源信息
     * @return 修改结果
     */
    ApiResult modifyResource(IResourcesEntity iResourcesEntity);

    /**
     * 通过id查询用户发布资源
     *
     * @param id 用户id
     * @return 查询结果
     */
    ApiResult findOne(Integer id);


}
