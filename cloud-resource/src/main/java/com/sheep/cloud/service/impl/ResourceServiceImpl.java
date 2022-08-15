package com.sheep.cloud.service.impl;

import com.sheep.cloud.dao.IResourcesEntityRepository;
import com.sheep.cloud.dao.IUsersEntityRepository;
import com.sheep.cloud.entity.IResourcesEntity;
import com.sheep.cloud.request.IResourceAddVO;
import com.sheep.cloud.request.IResourceModifyVO;
import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private IResourcesEntityRepository iResourcesEntityRepository;

    @Autowired
    private IUsersEntityRepository usersEntityRepository;
    /**
     * 发布资源
     *
     * @param vo 资源
     * @return 添加结果
     */
    @Override
    public ApiResult addOne(IResourceAddVO vo) {
        IResourcesEntity iResourcesEntity = new IResourcesEntity();
        if (!usersEntityRepository.existsById(vo.getPublishUser())) {
            return ApiResult.error("该用户不存在！");
        }
        iResourcesEntity.setName(vo.getName());
        iResourcesEntity.setDescription(vo.getDescription());
        iResourcesEntity.setLink(vo.getLink());
        iResourcesEntity.setIcon(vo.getIcon());
        iResourcesEntity.setPublishUser(usersEntityRepository.getOne(vo.getPublishUser()));
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
     * @param vo 修改资源信息
     * @return 修改结果
     */
    @Override
    public ApiResult modifyResource(IResourceModifyVO vo) {
        IResourcesEntity iResourcesEntity = iResourcesEntityRepository
                .findById(vo.getId())
                .orElseThrow(() -> new RuntimeException("改资源不存在！"));

        if (StringUtils.hasText(vo.getName())) {
            iResourcesEntity.setName(vo.getName());
        }
        if (StringUtils.hasText(vo.getDescription())) {
            iResourcesEntity.setDescription(vo.getDescription());
        }
        if (StringUtils.hasText(vo.getLink())) {
            iResourcesEntity.setLink(vo.getLink());
        }
        if (StringUtils.hasText(vo.getIcon())) {
            iResourcesEntity.setIcon(vo.getIcon());
        }
        if (StringUtils.hasText(vo.getContent())) {
            iResourcesEntity.setContent(vo.getContent());
        }
        if (!Objects.isNull(vo.getLabels())) {
            iResourcesEntity.setLabels(vo.getLabels());
        }
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
            return ApiResult.warning("该用户暂未分享资源");
        } else {
            return ApiResult.success(list);
        }
    }


}
