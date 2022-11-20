package com.sheep.cloud.controller;


import com.sheep.cloud.dto.request.knowledge.IResourceAddVO;
import com.sheep.cloud.dto.request.knowledge.IResourceModifyVO;
import com.sheep.cloud.dto.request.knowledge.IResourcePaymentVO;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @PostMapping("/add")
    public ApiResult<?> addOne(@RequestBody @Valid IResourceAddVO vo) {
        return resourceService.addOne(vo);
    }

    @DeleteMapping("/{id}")
    public ApiResult<?> deleteResource(@PathVariable Integer id) {
        return resourceService.deleteResourceById(id);
    }

    @PostMapping("/modifyResource")
    public ApiResult<?> modifyResource(@RequestBody @Valid IResourceModifyVO vo) {
        return resourceService.modifyResource(vo);
    }

    @GetMapping("/find/id")
    public ApiResult<?> findByResourceId(@RequestParam(required = false) Integer id) {
        if (id == null) {
            return new ApiResult<>().error("id不能为空");
        }
        return resourceService.findOneByResourceId(id);
    }

    @GetMapping("/find/one")
    public ApiResult<?> findOne(Integer id) {
        return resourceService.findOne(id);
    }

    @PostMapping("/payment")
    public ApiResult<?> payment(@RequestBody @Valid IResourcePaymentVO vo) {
        return resourceService.payment(vo);
    }

    @GetMapping("/label")
    public ApiResult<?> lable(@RequestParam(value = "id", required = false) Integer id,
                              @RequestParam(value = "pageNum", required = false) Integer pageNum,
                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        List<Integer> labels = Arrays.asList(id);
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }
        return resourceService.label(labels, pageNum - 1, pageSize);
    }

    @PostMapping("/add/collect")
    public ApiResult<?> addCollect(@RequestParam Integer uid, @RequestParam Integer rid) {
        return resourceService.addCollect(uid, rid);
    }

    @DeleteMapping("/collect/{id}")
    public ApiResult<?> deleteCollectById(@PathVariable Integer id) {
        return resourceService.deleteCollectById(id);
    }

    @DeleteMapping("/delete/collect")
    public ApiResult<?> deleteByResourceIdAndUserUid(@RequestParam Integer uid, @RequestParam Integer rid) {
        return resourceService.deleteByResourceIdAndUserUid(uid, rid);
    }

    @GetMapping("/find/collect")
    public ApiResult<?> findCollectById(Integer uid) {
        return resourceService.findAllByListIn(uid);
    }


    @GetMapping("/count")
    public ApiResult<?> countDistinctByLabelsId(@RequestParam Integer id) {
        return resourceService.countDistinctByLabelsId(id);
    }


    @GetMapping("/find/all")
    public ApiResult<?> test(@RequestParam(value = "id", required = false) Integer id,
                             @RequestParam(value = "order", required = false) int order,
                             @RequestParam(value = "isFree", required = false) boolean isFree,
                             @RequestParam(value = "pageNum", required = false) Integer pageNum,
                             @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        List<Integer> labels = Arrays.asList(id);
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }
        return resourceService.findAllResources(labels, order, isFree, pageNum - 1, pageSize);

    }

    @GetMapping("/find/dynamic")
    public ApiResult<?> findByDynamicSearch(
            @RequestParam(required = false) Integer labelId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer orderId,
            @RequestParam(required = false) Boolean isFree,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }
        return resourceService.findByDynamicSearch(labelId, name, orderId, isFree, pageNum - 1, pageSize);
    }

}
