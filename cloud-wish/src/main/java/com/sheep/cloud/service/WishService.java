package com.sheep.cloud.service;

import com.sheep.cloud.request.IWishHelpFinishVO;
import com.sheep.cloud.request.IWishPublishVO;
import com.sheep.cloud.response.ApiResult;

import java.util.List;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service
 * @datetime 2022/8/16 星期二
 */
public interface WishService {

    /**
     * 发布心愿
     *
     * @param wishPublishVO 心愿信息
     * @return 发布结果
     */
    ApiResult publishWish(IWishPublishVO wishPublishVO);


    /**
     * 获取所有心愿
     *
     * @param page 页码
     * @param size 每页数量
     * @return 查询结果
     */
    ApiResult getWishList(Integer page, Integer size);


    /**
     * 查询某个用户发布的心愿
     *
     * @param userId 用户id
     * @param page   页码
     * @param size   每页数量
     * @return 查询结果
     */
    ApiResult getWishListByUserId(Integer userId, Integer page, Integer size);

    /**
     * 根据id删除某条心愿
     *
     * @param id 心愿id
     * @return 删除结果
     */
    ApiResult deleteWishById(Integer id);

    /**
     * 根据标签查询心愿列表
     *
     * @param labelIds 标签id列表
     * @param page     页码
     * @param size     每页数量
     * @return 查询结果
     */
    ApiResult findByLabels(List<Integer> labelIds, Integer page, Integer size);


    /**
     * 帮助完成心愿
     *
     * @param vo 心愿信息
     * @return 完成结果
     */
    ApiResult helpFinishWish(IWishHelpFinishVO vo);
}
