package com.sheep.cloud.service.impl;

import com.sheep.cloud.dao.knowledge.ICommentsEntityRepository;
import com.sheep.cloud.dao.knowledge.IRepliesEntityRepository;
import com.sheep.cloud.entity.knowledge.ICommentsEntity;
import com.sheep.cloud.entity.knowledge.IRepliesEntity;
import com.sheep.cloud.entity.knowledge.IResourcesEntity;
import com.sheep.cloud.entity.knowledge.IUsersEntity;
import com.sheep.cloud.dto.request.knowledge.ICommentAddVO;
import com.sheep.cloud.dto.request.knowledge.IReplyAddVO;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.knowledge.ICommentGetInfoDTO;
import com.sheep.cloud.dto.response.knowledge.IReplyGetInfoDTO;
import com.sheep.cloud.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ALEX
 * @since 2022/8/13 14:37
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private ICommentsEntityRepository commentsEntityRepository;
    @Autowired
    private IRepliesEntityRepository repliesEntityRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ApiResult<?> deleteCommentById(Integer id) {
        //判断是否存在回复
        if (commentsEntityRepository.existsById(id)) {
            //存在回复则先删除回复再删除评论，回复不存在则直接删除评论
            if (repliesEntityRepository.existsCommentById(id) != 0) {
                repliesEntityRepository.deleteCommentById(id);
                commentsEntityRepository.deleteById(id);
                return new ApiResult<>().success("删除成功");
            } else {
                commentsEntityRepository.deleteById(id);
                return new ApiResult<>().success("删除成功");
            }
        }
        return new ApiResult<>().error("评论不存在");
    }

    @Override
    public ApiResult<?> deleteReplyById(Integer id) {
        if (repliesEntityRepository.existsById(id)) {
            repliesEntityRepository.deleteById(id);
            return new ApiResult<>().success("删除成功");
        } else {
            return new ApiResult<>().error("删除失败");
        }
    }


    /*
     * @Description: 发表评论
     * @param commentAddVo
     * @return: com.sheep.cloud.response.ApiResult
     */
    @Override
    public ApiResult<?> insertComment(ICommentAddVO commentAddVo) {
        ICommentsEntity comment = new ICommentsEntity();
        comment.setContent(commentAddVo.getContent());

        IUsersEntity iUsersEntity = new IUsersEntity();
        iUsersEntity.setUid(commentAddVo.getPublishUser());
        comment.setPublishUser(iUsersEntity);

        IResourcesEntity iResourcesEntity = new IResourcesEntity();
        iResourcesEntity.setId(commentAddVo.getResourceId());
        comment.setResourceId(iResourcesEntity);

        comment.setPublishTime(LocalDateTime.now());
        commentsEntityRepository.save(comment);
        return new ApiResult<>().success("发表评论成功");
    }


    /*
     * @Description: 发表回复
     * @param replyAddVO
     * @return: com.sheep.cloud.response.ApiResult
     */
    @Override
    public ApiResult<?> insertReply(IReplyAddVO replyAddVO) {
        IRepliesEntity reply = new IRepliesEntity();
        reply.setContent(replyAddVO.getContent());
        reply.setPublishTime(LocalDateTime.now());

        ICommentsEntity iCommentsEntity = new ICommentsEntity();
        iCommentsEntity.setId(replyAddVO.getCommentId());
        reply.setCommentId(iCommentsEntity);

        IRepliesEntity iRepliesEntity = new IRepliesEntity();
        iRepliesEntity.setId(replyAddVO.getLastReplyId());
        reply.setLastReplyId(iRepliesEntity);

        IUsersEntity iUsersEntity = new IUsersEntity();
        iUsersEntity.setUid(replyAddVO.getReplyUser());
        reply.setReplyUser(iUsersEntity);

        repliesEntityRepository.save(reply);
        return new ApiResult<>().success("回复成功");
    }

    /*
     * @Description: 根据资源id获取评论
     * @param resource_id
     * @return: com.sheep.cloud.response.ApiResult
     */
    @Override
    public ApiResult<ArrayList<ICommentGetInfoDTO>> getCommentByResourceId(Integer resource_id) {
        List<ICommentsEntity> commentList = commentsEntityRepository.findCommentByResourceId(resource_id);
        ArrayList<ICommentGetInfoDTO> list = new ArrayList<>();
        for (ICommentsEntity commentsEntity : commentList) {
            ICommentGetInfoDTO result = modelMapper.map(commentsEntity, ICommentGetInfoDTO.class);
            list.add(result);
        }
        return new ApiResult<ArrayList<ICommentGetInfoDTO>>().success(list);
    }

    /*
     * @Description: 根据评论id获取回复
     * @param comment_id
     * @return: com.sheep.cloud.response.ApiResult
     */
    @Override
    public ApiResult<?> getReplyByCommentId(Integer comment_id) {
        List<IRepliesEntity> replyList = repliesEntityRepository.getReplyByCommentId(comment_id);
        ArrayList<IReplyGetInfoDTO> list = new ArrayList<>();
        for (IRepliesEntity repliesEntity : replyList) {
            IReplyGetInfoDTO result = modelMapper.map(repliesEntity, IReplyGetInfoDTO.class);
            list.add(result);
        }
        return new ApiResult<ArrayList<IReplyGetInfoDTO>>().success(list);
    }
}
