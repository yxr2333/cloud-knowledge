package com.sheep.cloud.service.impl;

import com.sheep.cloud.dao.ICommentsPlatformEntityRepository;
import com.sheep.cloud.entity.ICommentsPlatformEntity;
import com.sheep.cloud.entity.IUsersEntity;
import com.sheep.cloud.request.ICommentPlatformAddVO;
import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.response.ICommentPlatformGetUserInfoDTO;
import com.sheep.cloud.service.CommentPlatformService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ALEX
 * @since 2022/8/15 13:18
 */
@Service
public class CommentPlatformServiceImpl implements CommentPlatformService {

    @Autowired
    private ICommentsPlatformEntityRepository commentsPlatformEntityRepository;

    @Autowired
    private ModelMapper modelMapper;

    /*
     * @Description: 在评论角发表评论
     * @param commentPlatformAddVO
     * @return: com.sheep.cloud.response.ApiResult
     */
    @Override
    public ApiResult insertComment(@RequestBody @Valid ICommentPlatformAddVO commentPlatformAddVO) {
        ICommentsPlatformEntity comment = new ICommentsPlatformEntity();
        comment.setContent(commentPlatformAddVO.getContent());
        comment.setPublishTime(LocalDateTime.now());

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
        if (commentsPlatformEntityRepository.existsById(id)) {
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
        List<ICommentsPlatformEntity> commentList = commentsPlatformEntityRepository.findAll();
        ArrayList<ICommentPlatformGetUserInfoDTO> list = new ArrayList<>();
        for (ICommentsPlatformEntity commentsEntity : commentList) {
            ICommentPlatformGetUserInfoDTO result = modelMapper.map(commentsEntity, ICommentPlatformGetUserInfoDTO.class);
            list.add(result);
        }
        return ApiResult.success(list);
    }
}