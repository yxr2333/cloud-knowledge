package com.sheep.cloud.controller;

import com.sheep.cloud.dto.request.knowledge.IWishHelpFinishVO;
import com.sheep.cloud.dto.request.knowledge.IWishPublishVO;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.WishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.controller
 * @datetime 2022/8/16 星期二
 */
@RestController
@RequestMapping("/wish")
@Slf4j
public class WishController {

    @Autowired
    private WishService wishService;

    @PostMapping("/publishWish")
    public ApiResult<?> publishWish(@RequestBody @Valid IWishPublishVO wishPublishVO) {
        return wishService.publishWish(wishPublishVO);
    }

    @GetMapping("/get/one/{id}")
    public ApiResult<?> getOne(@PathVariable Integer id) {
        if (id == null) {
            return new ApiResult<>().error("id不能为空");
        }
        return wishService.getWishById(id);
    }

    @GetMapping("/getWishListByUserId")
    public ApiResult<?> getWishListByUserId(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }
        if (userId == null) {
            return wishService.getWishList(pageNum - 1, pageSize);
        }
        return wishService.getWishListByUserId(userId, pageNum - 1, pageSize);
    }

    @GetMapping("/getWishList")
    public ApiResult<?> getWishList(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }
        return wishService.getWishList(pageNum - 1, pageSize);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResult<?> deleteWishById(@PathVariable Integer id) {
        return wishService.deleteWishById(id);
    }

    @PostMapping("/helpFinishWish")
    public ApiResult<?> helpFinishWish(@RequestBody @Valid IWishHelpFinishVO vo) {
        return wishService.helpFinishWish(vo);
    }

    @GetMapping("/findByContentAndLabels")
    public ApiResult<?> findWishesByContentAndLabels(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) int[] labels) {
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }
        if (content == null && (labels == null || labels.length == 0)) {
            return wishService.getWishList(pageNum - 1, pageSize);
        }
        List<Integer> ids = new ArrayList<>();
        if (labels != null && labels.length != 0) {
            ids = Arrays.stream(labels).boxed().collect(Collectors.toList());
        }
        return wishService.findWishesByContentAndLabels(content, ids, pageNum - 1, pageSize);
    }
}
