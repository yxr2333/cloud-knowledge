package com.sheep.cloud.service;

import com.sheep.cloud.request.ICommentAddVO;
import com.sheep.cloud.request.IReplyAddVO;
import com.sheep.cloud.response.ApiResult;

/**
 * @author ALEX
 * @since 2022/8/13 14:36
 */
public interface CommentService {
    /*
     * @Description: 发表评论
     * @param commentsEntity
     * @return: com.sheep.cloud.response.ApiResult
     */
    ApiResult insertComment(ICommentAddVO commentAddVo);

    /*
     * @Description: 发表回复
     * @param replyAddVO
     * @return: com.sheep.cloud.response.ApiResult
     */
    ApiResult insertReply(IReplyAddVO replyAddVO);

    /*
     * @Description:根据id删除评论
     * @param id
     * @return: com.sheep.cloud.response.ApiResult
     */
    ApiResult deleteCommentById(Integer id);

    /*
     * @Description: 根据评论id删除回复
     * @param id
     * @return: com.sheep.cloud.response.ApiResult
     */
    ApiResult deleteReplyById(Integer id);

    /*
     * @Description: 根据资源id查询评论
     * @param resource_id
     * @return: com.sheep.cloud.response.ApiResult
     */
    ApiResult getCommentByResourceId(Integer resource_id);

    /*
     * @Description: 根据评论id获取回复
     * @param comment_id
     * @return: com.sheep.cloud.response.ApiResult
     */
    ApiResult getReplyByCommentId(Integer comment_id);
}