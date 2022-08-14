package com.sheep.cloud.controller;

import com.sheep.cloud.request.ICommentAddVO;
import com.sheep.cloud.request.IReplyAddVO;
import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.service.CommentService;
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

    @DeleteMapping("/deleteComment/{id}")
    public ApiResult deleteComment(@PathVariable(value = "id") Integer id){
        return commentService.deleteCommentById(id);
    }

    @DeleteMapping("/deleteReply/{id}")
    public ApiResult deleteReply(@PathVariable(value = "id") Integer id){
        return commentService.deleteReplyById(id);
    }

    @PostMapping("/addComment")
    public ApiResult addContent(ICommentAddVO iCommentAddVo){
        return commentService.insertComment(iCommentAddVo);
    }

    @PostMapping("/addReply")
    public ApiResult addReply(IReplyAddVO iReplyAddVO){
        return commentService.insertReply(iReplyAddVO);
    }

    @PostMapping("/collect")
    public ApiResult collectById(Integer id){
        return commentService.collectById(id);
    }
}
