package com.sheep.cloud.service.impl;

import com.sheep.cloud.dao.ICollectListsEntityRepository;
import com.sheep.cloud.dao.ILabelsEntityRepository;
import com.sheep.cloud.dao.IResourcesEntityRepository;
import com.sheep.cloud.dao.IUsersEntityRepository;
import com.sheep.cloud.entity.ICollectListsEntity;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    private ICollectListsEntityRepository iCollectListsEntityRepository;

    @Autowired
    private ModelMapper modelMapper;
    private Object LocalDate;

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
        iResourcesEntity.setReleaseTime(LocalDateTime.now());
        iResourcesEntity.setIsPaid(vo.getIsPaid());
        if (vo.getIsPaid()) {
            if (!StringUtils.isEmpty(vo.getPassword())) {
                iResourcesEntity.setPassword(vo.getPassword());
            } else {
                return ApiResult.warning("付费资源需要设置资源访问密码！");
            }
        }
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
     * 修改资源信息
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
     * @param labelId  标签
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

    /**
     * 用户收藏资源
     *
     * @param uid 用户id
     * @param rid 资源id
     * @return 收藏结果
     */
    @Override
    public ApiResult addCollect(Integer uid, Integer rid) {
        if (!usersEntityRepository.existsById(uid)) {
            return ApiResult.error("该用户不存在！");
        }
        if (!iResourcesEntityRepository.existsById(rid)) {
            return ApiResult.error("该资源不存在！");
        }
        IResourcesEntity iResourcesEntity = iResourcesEntityRepository.getOne(rid);
        if (iCollectListsEntityRepository.findICollectListsEntityIdByResourceIdAndUserUid(rid, uid) != null) {
            return ApiResult.warning("请不要重复收藏该资源！");
        }
        //收藏量加一
        if (iResourcesEntity.getCollect() == null) {
            iResourcesEntity.setCollect(1);
        } else {
            iResourcesEntity.setCollect(iResourcesEntity.getCollect() + 1);
        }
        iResourcesEntityRepository.save(iResourcesEntity);
        ICollectListsEntity iCollectListsEntity = new ICollectListsEntity();
        iCollectListsEntity.setUser(usersEntityRepository.getOne(uid));
        iCollectListsEntity.setResource(iResourcesEntityRepository.getOne(rid));
        iCollectListsEntity.setCreateTime(LocalDateTime.now());
        iCollectListsEntityRepository.save(iCollectListsEntity);
        return ApiResult.success("收藏成功！");
    }

    /**
     * 通过id用户取消收藏资源
     *
     * @param id 收藏id
     * @return 取消收藏结果
     */
    @Override
    public ApiResult deleteCollectById(Integer id) {
        if (iCollectListsEntityRepository.existsById(id)) {
            ICollectListsEntity iCollectListsEntity = iCollectListsEntityRepository.getOne(id);
            IResourcesEntity iResourcesEntity = iResourcesEntityRepository
                    .findById(iCollectListsEntity.getResource().getId())
                    .orElseThrow(() -> new RuntimeException("该资源不存在！"));
            //收藏量减一
            iResourcesEntity.setCollect(iResourcesEntity.getCollect() - 1);
            iResourcesEntityRepository.save(iResourcesEntity);
            iCollectListsEntityRepository.deleteById(id);
            return ApiResult.success("取消收藏成功！");
        } else {
            return ApiResult.error("收藏不存在！");
        }
    }

    /**
     * 通过用户id和资源id取消收藏资源
     *
     * @param uid 用户id
     * @param rid 资源id
     * @return 收藏结果
     */
    @Override
    @Transactional
    public ApiResult deleteByResourceIdAndUserUid(Integer uid, Integer rid) {
        if (!usersEntityRepository.existsById(uid)) {
            return ApiResult.error("该用户不存在！");
        }
        iCollectListsEntityRepository.deleteByResourceIdAndUserUid(uid, rid);
        IResourcesEntity iResourcesEntity = iResourcesEntityRepository
                .findById(rid)
                .orElseThrow(() -> new RuntimeException("该资源不存在！"));
        if (iCollectListsEntityRepository.findICollectListsEntityIdByResourceIdAndUserUid(rid, uid) == null) {
            return ApiResult.warning("请勿重复取消收藏！");
        }
        //收藏量减一
        iResourcesEntity.setCollect(iResourcesEntity.getCollect() - 1);
        iResourcesEntityRepository.save(iResourcesEntity);
        iCollectListsEntityRepository.deleteByResourceIdAndUserUid(rid, uid);
        return ApiResult.success("取消收藏成功！");
    }

    /**
     * 查询用户收藏列表
     *
     * @param uid 用户id
     * @return 查询结果
     */
    @Override
    public ApiResult findAllByListIn(Integer uid) {
        if (!usersEntityRepository.existsById(uid)) {
            return ApiResult.error("该用户不存在！");
        }
        List<IResourcesEntity> list = iResourcesEntityRepository.findAllByUserId(uid);
        if (CollectionUtils.isEmpty(list)) {
            return ApiResult.warning("该用户暂无收藏记录！");
        } else {
            return ApiResult.success(list);
        }
    }

    /**
     * 查询指定标签下的资源个数
     *
     * @param id 标签id
     * @return 查询结果
     */
    @Override
    public ApiResult countDistinctByLabelsId(Integer id) {
        return ApiResult.success(iResourcesEntityRepository.countDistinctByLabelsId(id));
    }

    /**
     * 查询所有资源
     *
     * @param order    排序规则（0：不排序、1：按收藏量排序、2按发布时间排序）
     * @param isFree   资源筛选是否免费（0：免费，1：付费）
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 查询结果
     */
    @Override
    public ApiResult findAllResources(List<Integer> labelId, int order, boolean isFree, Integer pageNum, Integer pageSize) {

        ArrayList<ILabelsEntity> labels = new ArrayList<>();
        // 根据标签编号查询标签
        labelId.forEach(id -> {
            ILabelsEntity labelsEntity = iLabelsEntityRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("标签不存在"));
            labels.add(labelsEntity);
        });


        Sort sort = null;
        PageRequest pageable = null;
        if (order == 0) {
            pageable = PageRequest.of(pageNum, pageSize);
        } else if (order == 1) {
            sort = Sort.by(Sort.Direction.DESC, "collect");
            pageable = PageRequest.of(pageNum, pageSize, sort);
        } else if (order == 2) {
            sort = Sort.by(Sort.Direction.DESC, "releaseTime");
            pageable = PageRequest.of(pageNum, pageSize, sort);
        } else {
            return ApiResult.warning("请输入正确的排序规则！");
        }

        Page<IResourcesEntity> page = iResourcesEntityRepository.findDistinctAllByIsPaidAndAndLabelsIn(isFree, labels, pageable);

        PageData.PageDataBuilder<IResourcesEntity> builder = PageData.builder();
        return ApiResult.success(builder.totalPage(page.getTotalPages())
                .totalNum(page.getTotalElements())
                .data(page.getContent())
                .build());
    }


    /**
     * 根据资源id查询资源
     *
     * @param id 资源id
     * @return 查询结果
     */
    @Override
    public ApiResult findOneByResourceId(Integer id) {
        IResourcesEntity entity = iResourcesEntityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("该资源不存在！"));
        return ApiResult.success(entity);
    }

    /**
     * 复杂查询
     *
     * @param labelId  标签编号
     * @param name     资源名称
     * @param orderId  排序方式
     * @param isFree   是否免费
     * @param pageNum  页码
     * @param pageSize 页容量
     * @return 查询结果
     */
    @Override
    public ApiResult findByDynamicSearch(Integer labelId, String name, Integer orderId, Boolean isFree, Integer pageNum, Integer pageSize) {
        if (labelId == null) {
            return ApiResult.error("请选择资源分类");
        }
        List<Integer> labelIds = Arrays.asList(labelId);
        List<ILabelsEntity> labels = iLabelsEntityRepository.findAllById(labelIds);
        Sort sort = null;
        if (orderId == 0) {
            sort = Sort.by(Sort.Direction.DESC, "releaseTime");
        } else {
            sort = Sort.by(Sort.Direction.DESC, "collect");
        }
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<IResourcesEntity> page = null;
        if (!StringUtils.hasText(name)) {
            page = iResourcesEntityRepository.findAllByIsPaidAndLabelsIn(!isFree, labels, pageable);
        } else {
            name = "%" + name + "%";
            page = iResourcesEntityRepository.findAllByIsPaidAndNameLikeIgnoreCaseAndLabelsIn(!isFree, name, labels, pageable);
        }
        PageData.PageDataBuilder<IResourcesEntity> builder = PageData.builder();
        return ApiResult.success(builder.totalPage(page.getTotalPages())
                .totalNum(page.getTotalElements())
                .data(page.getContent())
                .build());
    }
}
