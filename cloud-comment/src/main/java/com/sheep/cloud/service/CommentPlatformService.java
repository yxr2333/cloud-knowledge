package com.sheep.cloud.service;

import com.sheep.cloud.dto.request.knowledge.ICommentPlatformAddVO;
import com.sheep.cloud.dto.response.ApiResult;

/**
 * @author ALEX
 * @since 2022/8/15 13:18
 */
public interface CommentPlatformService {
    /*
     * @Description:在评论角发表评论
     * @param commentPlatformAddVO
     * @return: com.sheep.cloud.response.ApiResult
     */
    ApiResult<?> insertComment(ICommentPlatformAddVO commentPlatformAddVO);

    /*
     * @Description:根据id删除评论角评论
     * @param id
     * @return: com.sheep.cloud.response.ApiResult
     */
    ApiResult<?> deleteCommentById(Integer id);

    /*
     * @Description: 获取所有评论
     * @param id
     * @return: com.sheep.cloud.response.ApiResult
     */
    ApiResult<?> getAllComment();
}
