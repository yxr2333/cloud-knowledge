package com.sheep.cloud.controller;


import com.sheep.cloud.entity.IResourcesEntity;
import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @PostMapping("/add")
    public ApiResult addOne(@RequestBody @Valid IResourcesEntity iResourcesEntity) {
        return resourceService.addOne(iResourcesEntity);
    }

    @DeleteMapping("/{id}")
    public ApiResult deleteResource(@PathVariable Integer id) {
        return resourceService.deleteResourceById(id);
    }

    @PostMapping("/modifyResource")
    public ApiResult modifyResource(@RequestBody @Valid IResourcesEntity iResourcesEntity) {
        return resourceService.modifyResource(iResourcesEntity);
    }

    @GetMapping("/find/one")
    public ApiResult findOne(Integer id) {
        return resourceService.findOne(id);
    }
}
