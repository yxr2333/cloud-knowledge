package com.sheep.cloud.controller;

import com.sheep.cloud.request.IWishHelpFinishVO;
import com.sheep.cloud.request.IWishPublishVO;
import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.service.WishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ApiResult publishWish(@RequestBody @Valid IWishPublishVO wishPublishVO) {
        return wishService.publishWish(wishPublishVO);
    }

    @GetMapping("/getWishListByUserId")
    public ApiResult getWishListByUserId(
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
    public ApiResult getWishList(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }
        return wishService.getWishList(pageNum - 1, pageSize);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResult deleteWishById(@PathVariable Integer id) {
        return wishService.deleteWishById(id);
    }

    @PostMapping("/helpFinishWish")
    public ApiResult helpFinishWish(@RequestBody @Valid IWishHelpFinishVO vo) {
        return wishService.helpFinishWish(vo);
    }
}
