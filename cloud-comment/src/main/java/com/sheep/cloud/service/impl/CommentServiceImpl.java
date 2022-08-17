package com.sheep.cloud.service.impl;

import com.sheep.cloud.dao.ICommentsEntityRepository;
import com.sheep.cloud.dao.IRepliesEntityRepository;
import com.sheep.cloud.entity.ICommentsEntity;
import com.sheep.cloud.entity.IRepliesEntity;
import com.sheep.cloud.entity.IResourcesEntity;
import com.sheep.cloud.entity.IUsersEntity;
import com.sheep.cloud.request.ICommentAddVO;
import com.sheep.cloud.request.IReplyAddVO;
import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.service.CommentService;
import com.sheep.cloud.response.ICommentGetInfoDTO;
import com.sheep.cloud.response.IReplyGetInfoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
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
    public ApiResult deleteCommentById(Integer id) {
        //判断是否存在回复
        if(commentsEntityRepository.existsById(id)) {
            //存在回复则先删除回复再删除评论，回复不存在则直接删除评论
            if (repliesEntityRepository.existsCommentById(id)!=0) {
                repliesEntityRepository.deleteCommentById(id);
                commentsEntityRepository.deleteById(id);
                return ApiResult.success("删除成功");
            } else{
                commentsEntityRepository.deleteById(id);
                return ApiResult.success("删除成功");
            }
        }
        return ApiResult.success("评论不存在");
    }

    @Override
    public ApiResult deleteReplyById(Integer id) {
        if(repliesEntityRepository.existsById(id)){
            repliesEntityRepository.deleteById(id);
            return ApiResult.success("删除成功");
        }
        else
            return ApiResult.error("删除失败");
    }


    /*
     * @Description: 发表评论
     * @param commentAddVo
     * @return: com.sheep.cloud.response.ApiResult
     */
    @Override
    public ApiResult insertComment(ICommentAddVO commentAddVo) {
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
        return ApiResult.success("发表评论成功");
    }



    /*
     * @Description: 发表回复
     * @param replyAddVO
     * @return: com.sheep.cloud.response.ApiResult
     */
    @Override
    public ApiResult insertReply(IReplyAddVO replyAddVO) {
        IRepliesEntity reply = new IRepliesEntity();
        reply.setContent(replyAddVO.getContent());
        reply.setPublishTime(new Timestamp(new Date().getTime()));

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
        return ApiResult.success("回复成功");
    }

    /*
     * @Description: 根据资源id获取评论
     * @param resource_id
     * @return: com.sheep.cloud.response.ApiResult
     */
    @Override
    public ApiResult getCommentByResourceId(Integer resource_id) {
        List<ICommentsEntity> commentList = commentsEntityRepository.findCommentByResourceId(resource_id);
        ArrayList<ICommentGetInfoDTO> list = new ArrayList<>();
        for(ICommentsEntity commentsEntity : commentList){
            ICommentGetInfoDTO result = modelMapper.map(commentsEntity, ICommentGetInfoDTO.class);
            list.add(result);
        }
        return ApiResult.success(list);
    }

    /*
     * @Description: 根据评论id获取回复
     * @param comment_id
     * @return: com.sheep.cloud.response.ApiResult
     */
    @Override
    public ApiResult getReplyByCommentId(Integer comment_id) {
        List<IRepliesEntity> replyList = repliesEntityRepository.getReplyByCommentId(comment_id);
        ArrayList<IReplyGetInfoDTO> list = new ArrayList<>();
        for(IRepliesEntity repliesEntity : replyList){
            IReplyGetInfoDTO result = modelMapper.map(repliesEntity, IReplyGetInfoDTO.class);
            list.add(result);
        }
        return ApiResult.success(list);
    }
}
