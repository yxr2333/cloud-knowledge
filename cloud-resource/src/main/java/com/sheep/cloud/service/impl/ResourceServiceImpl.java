package com.sheep.cloud.service.impl;

import com.sheep.cloud.dao.ILabelsEntityRepository;
import com.sheep.cloud.dao.IResourcesEntityRepository;
import com.sheep.cloud.dao.IUsersEntityRepository;
import com.sheep.cloud.entity.ILabelsEntity;
import com.sheep.cloud.entity.IResourcesEntity;
import com.sheep.cloud.request.IResourceAddVO;
import com.sheep.cloud.request.IResourceModifyVO;
import com.sheep.cloud.request.IResourcePaymentVO;
import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.response.PageData;
import com.sheep.cloud.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private IResourcesEntityRepository iResourcesEntityRepository;

    @Autowired
    private IUsersEntityRepository usersEntityRepository;
    @Autowired
    private ILabelsEntityRepository iLabelsEntityRepository;

    @Autowired
    private ModelMapper modelMapper;
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
        iResourcesEntity.setLabels(vo.getLabels());
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
                .orElseThrow(() -> new RuntimeException("该资源不存在！"));

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

    /**
     * 设置资源付费
     *
     * @param vo 资源付费信息
     * @return 修改结果
     */
    @Override
    public ApiResult payment(IResourcePaymentVO vo) {
        IResourcesEntity iResourcesEntity = iResourcesEntityRepository
                .findById(vo.getId())
                .orElseThrow(() -> new RuntimeException("该资源不存在！"));

        if (vo.getIsPaid()) {
            if (StringUtils.hasText(vo.getPassword())) {
                iResourcesEntity.setIsPaid(vo.getIsPaid());
                iResourcesEntity.setPassword(vo.getPassword());
            } else {
                return ApiResult.warning("付费资源需设置资源访问密码！");
            }
        } else {
            iResourcesEntity.setIsPaid(vo.getIsPaid());
            iResourcesEntity.setPassword("");
        }
        iResourcesEntityRepository.save(iResourcesEntity);
        return ApiResult.success("资源付费修改成功！");
    }

    /**
     * 根据标签划分资源
     *
     * @param labelId    标签
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 查询结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult label(List<Integer> labelId, Integer pageNum, Integer pageSize) {
        ArrayList<ILabelsEntity> labels = new ArrayList<>();
        // 根据标签编号查询标签
        labelId.forEach(id -> {
            ILabelsEntity labelsEntity = iLabelsEntityRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("标签不存在"));
            labels.add(labelsEntity);
        });
        PageRequest pageable = PageRequest.of(pageNum, pageSize);
        Page<IResourcesEntity> page = iResourcesEntityRepository.findDistinctAllByLabelsIn(labels, pageable);

        PageData.PageDataBuilder<IResourcesEntity> builder = PageData.builder();
        return ApiResult.success(builder.totalPage(page.getTotalPages())
                .totalNum(page.getTotalElements())
                .data(page.getContent())
                .build());
    }




}
