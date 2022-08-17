package com.sheep.cloud.service.impl;

import com.sheep.cloud.dao.ICommentsPlatformEntityRepository;
import com.sheep.cloud.entity.ICommentsPlatformEntity;
import com.sheep.cloud.entity.IUsersEntity;
import com.sheep.cloud.request.ICommentPlatformAddVO;
import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.service.CommentPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author ALEX
 * @since 2022/8/15 13:18
 */
@Service
public class CommentPlatformServiceImpl implements CommentPlatformService {

    @Autowired
    private ICommentsPlatformEntityRepository commentsPlatformEntityRepository;

    /*
     * @Description: 在评论角发表评论
     * @param commentPlatformAddVO
     * @return: com.sheep.cloud.response.ApiResult
     */
    @Override
    public ApiResult insertComment(ICommentPlatformAddVO commentPlatformAddVO) {
        ICommentsPlatformEntity comment = new ICommentsPlatformEntity();
        comment.setContent(commentPlatformAddVO.getContent());
        comment.setPublishTime(new Timestamp(new Date().getTime()));

        IUsersEntity iUsersEntity = new IUsersEntity();
        iUsersEntity.setUid(commentPlatformAddVO.getPublishUser());
        comment.setPublishUser(iUsersEntity);

        commentsPlatformEntityRepository.save(comment);
        return ApiResult.success("发表评论成功");
    }

    /*
     * @Description: 根据id删除评论角评论
     * @param id
     * @return: com.sheep.cloud.response.ApiResult
     */
    @Override
    public ApiResult deleteCommentById(Integer id) {
        if(commentsPlatformEntityRepository.existsById(id)){
            commentsPlatformEntityRepository.deleteById(id);
            return ApiResult.success("删除成功");
        }
        return ApiResult.error("评论不存在");
    }

    /*
     * @Description: 查询评论角所有评论
     * @param
     * @return: com.sheep.cloud.response.ApiResult
     */
    @Override
    public ApiResult getAllComment() {
        List<ICommentsPlatformEntity> data = commentsPlatformEntityRepository.findAll();
        return ApiResult.success(data);
    }
}