package com.sheep.cloud.controller;

import com.sheep.cloud.dto.request.knowledge.*;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
 * @datetime 2022/8/12 星期五
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ApiResult<?> doLogin(@RequestBody @Valid IUsersLoginVO vo) {
        return userService.doLogin(vo);
    }

    @PostMapping("/register/remote")
    public ApiResult<?> doRemoteRegister(@RequestBody @Valid IUsersRegisterVO vo) {
        return userService.remoteMakeUserRegister(vo);
    }

    @PostMapping("/register")
    public ApiResult<?> doRegister(@RequestBody @Valid IUsersRegisterVO vo) {
        return userService.doRegister(vo);
    }

    @PostMapping("/resetPassword")
    public ApiResult<?> resetPassword(HttpServletRequest request, @RequestBody @Valid IUsersResetPasswordVO vo) {
        return userService.resetPassword(request, vo);
    }

    @PostMapping("/modifyInfo")
    public ApiResult<?> modifyInfo(@RequestBody @Valid IUsersModifyInfoVO vo) {
        return userService.modifyInfo(vo);
    }

    @DeleteMapping("/{id}")
    public ApiResult<?> deleteUser(@PathVariable Integer id) {
        return userService.deleteUserById(id);
    }

    @GetMapping("/get/one")
    public ApiResult<?> getOne(@RequestParam(value = "id", required = false) Integer id) {
        return userService.getOne(id);
    }

    @GetMapping("/getAllLikeName")
    public ApiResult<?> getAllLikeName(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }
        if (StringUtils.isEmpty(name)) {
            return userService.getAll(pageNum - 1, pageSize);
        }
        return userService.getAllLikeName("%" + name + "%", pageNum - 1, pageSize);
    }

    @GetMapping("/findAllByLabelId")
    public ApiResult<?> findAllByLabelId(
            @RequestParam(value = "label", required = false) int[] labels,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }
        // labels数组转集合
        List<Integer> labelIds = Arrays.stream(labels).boxed().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(labelIds)) {
            return userService.getAll(pageNum - 1, pageSize);
        }
        return userService.findAllByLabelId(labelIds, pageNum - 1, pageSize);
    }


    @PostMapping("/addScore")
    public ApiResult<?> addScore(@RequestBody @Valid IUsersAddScoreVO vo) {
        return userService.addScore(vo);
    }

    @GetMapping("/find/list/score")
    public ApiResult<?> findScoreList(Integer uid) {
        return userService.findScoreList(uid);
    }

    @GetMapping("/find/list/wish")
    public ApiResult<?> findWishList(Integer uid) {
        return userService.findWishList(uid);
    }

    @GetMapping("/find/list/publish")
    public ApiResult<?> findPublishList(Integer uid) {
        return userService.findPublishList(uid);
    }

    @GetMapping("/find/list/collect")
    public ApiResult<?> findCollectList(Integer uid) {
        return userService.findCollectList(uid);
    }

    @GetMapping("/findAllByNameAndLabelId")
    public ApiResult<?> findAllByNameAndLabelId(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) int[] labelIds,
            @RequestParam(required = false) String name) {
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }
        if (name == null && (labelIds == null || labelIds.length == 0)) {
            return userService.getAll(pageNum - 1, pageSize);
        }
        List<Integer> ids = new ArrayList<>();
        if (labelIds != null && labelIds.length != 0) {
            ids = Arrays.stream(labelIds).boxed().collect(Collectors.toList());
        }
        return userService.findAllByNameAndLabelId(name, ids, pageNum - 1, pageSize);
    }

}
