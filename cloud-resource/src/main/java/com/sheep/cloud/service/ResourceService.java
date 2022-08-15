package com.sheep.cloud.service;

import com.sheep.cloud.request.IResourceAddVO;
import com.sheep.cloud.request.IResourceModifyVO;
import com.sheep.cloud.request.IResourcePaymentVO;
import com.sheep.cloud.response.ApiResult;

import java.util.List;

public interface ResourceService {
    /**
     * 发布资源
     *
     * @param vo 资源
     * @return 添加结果
     */
    ApiResult addOne(IResourceAddVO vo);

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
     * @param vo 修改资源信息
     * @return 修改结果
     */
    ApiResult modifyResource(IResourceModifyVO vo);

    /**
     * 通过id查询用户发布资源
     *
     * @param id 用户id
     * @return 查询结果
     */
    ApiResult findOne(Integer id);

    /**
     * 设置资源付费
     *
     * @param vo 资源付费信息
     * @return 修改结果
     */
    ApiResult payment(IResourcePaymentVO vo);

    /**
     * 根据标签划分资源
     *
     * @param labels     标签
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 查询结果
     */
    ApiResult label(List<Integer> labels, Integer pageNum, Integer pageSize);
}
