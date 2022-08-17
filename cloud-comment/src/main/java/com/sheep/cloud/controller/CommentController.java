package com.sheep.cloud.controller;

import com.sheep.cloud.request.ICommentAddVO;
import com.sheep.cloud.request.IReplyAddVO;
import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.service.CommentService;
import com.sheep.cloud.request.ICommentPlatformAddVO;
import com.sheep.cloud.service.CommentPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ALEX
 * @since 2022/8/13 15:24
 */

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentPlatformService commentPlatformService;

    @PostMapping("/addComment")
    public ApiResult addContent(ICommentAddVO iCommentAddVo){
        return commentService.insertComment(iCommentAddVo);
    }

    @PostMapping("/addReply")
    public ApiResult addReply(IReplyAddVO iReplyAddVO){
        return commentService.insertReply(iReplyAddVO);
    }

    @DeleteMapping("/deleteComment/{id}")
    public ApiResult deleteComment(@PathVariable(value = "id") Integer id){
        return commentService.deleteCommentById(id);
    }

    @DeleteMapping("/deleteReply/{id}")
    public ApiResult deleteReply(@PathVariable(value = "id") Integer id){
        return commentService.deleteReplyById(id);
    }

    @GetMapping("/getComment")
    public ApiResult getComment(Integer resource_id){
        return commentService.getCommentByResourceId(resource_id);
    }

    @GetMapping("/getReply")
    public ApiResult getReply(Integer comment_id){
        return commentService.getReplyByCommentId(comment_id);
    }

    @PostMapping("/addPlatform")
    public ApiResult getCommentFromPlatform(ICommentPlatformAddVO commentPlatformAddVO){
        return commentPlatformService.insertComment(commentPlatformAddVO);
    }

    @DeleteMapping("/deletePlatform/{id}")
    public ApiResult deleteCommentFromPlatform(@PathVariable(value = "id")Integer id){
        return commentPlatformService.deleteCommentById(id);
    }

    @GetMapping("/getPlatform")
    public ApiResult getPlatformFromPlatform(){
        return commentPlatformService.getAllComment();
    }
}
