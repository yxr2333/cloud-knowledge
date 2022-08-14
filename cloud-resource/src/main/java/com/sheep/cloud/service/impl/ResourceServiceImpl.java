package com.sheep.cloud.service.impl;

import com.sheep.cloud.dao.IResourcesEntityRepository;
import com.sheep.cloud.entity.IResourcesEntity;
import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private IResourcesEntityRepository iResourcesEntityRepository;


    /**
     * 发布资源
     *
     * @param iResourcesEntity 资源
     * @return 添加结果
     */
    @Override
    public ApiResult addOne(IResourcesEntity iResourcesEntity) {
        iResourcesEntityRepository.save(iResourcesEntity);
        return ApiResult.success("发表成功！");
    }


    /**
     * 通过用户名id删除资源
     *
     * @param id 资源id
     * @return 删除结果
     */
    @Override
    public ApiResult deleteResourceById(Integer id) {
        if (iResourcesEntityRepository.existsById(id)) {
            iResourcesEntityRepository.deleteById(id);
            return ApiResult.success("删除成功！");
        } else {
            return ApiResult.error("资源不存在！");
        }
    }

    /**
     * 用户修改信息
     *
     * @param iResourcesEntity 修改资源信息
     * @return 修改结果
     */
    @Override
    public ApiResult modifyResource(IResourcesEntity iResourcesEntity) {
        iResourcesEntityRepository.save(iResourcesEntity);
        return ApiResult.success("修改更新成功");
    }

    /**
     * 通过id查询用户发布资源
     *
     * @param id 用户id
     * @return 查询结果
     */
    @Override
    public ApiResult findOne(Integer id) {
        List<IResourcesEntity> list = iResourcesEntityRepository.findAllByPublishUserUid(id);
        if (CollectionUtils.isEmpty(list)) {
            return ApiResult.warning("暂未发表资源！");
        } else {
            return ApiResult.success(list);
        }
    }


}
