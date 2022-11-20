package com.sheep.cloud.service.impl;

import com.sheep.cloud.dao.knowledge.*;
import com.sheep.cloud.entity.knowledge.*;
import com.sheep.cloud.dto.request.knowledge.IWishHelpFinishVO;
import com.sheep.cloud.dto.request.knowledge.IWishPublishVO;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.knowledge.IWishBaseInfoDTO;
import com.sheep.cloud.dto.response.PageData;
import com.sheep.cloud.service.WishService;
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
import java.util.List;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service.impl
 * @datetime 2022/8/16 星期二
 */
@Service
@Slf4j
public class WishServiceImpl implements WishService {

    @Autowired
    private IWishReplyListEntityRepository wishReplyListEntityRepository;

    @Autowired
    private IWishesEntityRepository wishesEntityRepository;

    @Autowired
    private IUsersEntityRepository usersEntityRepository;

    @Autowired
    private ILabelsEntityRepository labelsEntityRepository;


    @Autowired
    private IResourcesEntityRepository resourcesEntityRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * 发布心愿
     *
     * @param wishPublishVO 心愿信息
     * @return 发布结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> publishWish(IWishPublishVO wishPublishVO) {
        IWishesEntity entity = new IWishesEntity();
        if (wishPublishVO.getContent().length() > 60) {
            return new ApiResult<>().error("心愿内容不能超过60个字");
        }
        entity.setContent(wishPublishVO.getContent());
        // 判断是否存在
        IUsersEntity user = usersEntityRepository
                .findById(wishPublishVO.getPublishUserId())
                .orElseThrow(() -> new RuntimeException("不存在此发布者的信息"));
        entity.setUser(user);
        // 设置心愿的标签
        ArrayList<ILabelsEntity> labels = new ArrayList<>();
        wishPublishVO.getLabelIds().forEach(labelId -> {
            labels.add(labelsEntityRepository
                    .findById(labelId)
                    .orElseThrow(() -> new RuntimeException("请选择正确的标签")));
        });
        entity.setLabels(labels);
        entity.setPublishTime(LocalDateTime.now());
        entity.setIsFinished(false);
        wishesEntityRepository.save(entity);
        return new ApiResult<>().success("发布成功");
    }

    /**
     * 获取所有心愿
     *
     * @param page 页码
     * @param size 每页数量
     * @return 查询结果
     */
    @Override
    public ApiResult<?> getWishList(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "publishTime");
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<IWishesEntity> resultPage = wishesEntityRepository.findAll(pageable);
        PageData<IWishBaseInfoDTO> result = toResponseResult(resultPage);
        return new ApiResult<PageData<IWishBaseInfoDTO>>().success(result);
    }

    /**
     * 根据编号获取心愿信息
     *
     * @param id 心愿编号
     * @return 心愿信息
     */
    @Override
    public ApiResult<?> getWishById(Integer id) {
        IWishesEntity entity = wishesEntityRepository.findById(id).orElseThrow(() -> new RuntimeException("不存在此心愿的信息"));
        return new ApiResult<>().success(entity);
    }

    private PageData<IWishBaseInfoDTO> toResponseResult(Page<IWishesEntity> resultPage) {
        ArrayList<IWishBaseInfoDTO> data = new ArrayList<>();
        resultPage.getContent().forEach(item -> {
            data.add(modelMapper.map(item, IWishBaseInfoDTO.class));
        });
        PageData.PageDataBuilder<IWishBaseInfoDTO> builder = PageData.builder();
        return builder.totalPage(resultPage.getTotalPages())
                .totalNum(resultPage.getTotalElements())
                .data(data)
                .build();
    }

    /**
     * 查询某个用户发布的心愿
     *
     * @param userId 用户id
     * @param page   页码
     * @param size   每页数量
     * @return 查询结果
     */
    @Override
    public ApiResult<?> getWishListByUserId(Integer userId, Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "publishTime");
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<IWishesEntity> pageData = wishesEntityRepository.findAllByUserUid(userId, pageable);
        PageData<IWishBaseInfoDTO> result = toResponseResult(pageData);
        return new ApiResult<PageData<IWishBaseInfoDTO>>().success(result);
    }

    /**
     * 根据id删除某条心愿
     *
     * @param id 心愿id
     * @return 删除结果
     */
    @Override
    public ApiResult<?> deleteWishById(Integer id) {
        if (!wishesEntityRepository.existsById(id)) {
            return new ApiResult<>().error("不存在此数据");
        }
        wishesEntityRepository.deleteById(id);
        return new ApiResult<>().success("删除成功");
    }

    /**
     * 根据标签查询心愿列表
     *
     * @param labelIds 标签id列表
     * @param page     页码
     * @param size     每页数量
     * @return 查询结果
     */
    @Override
    public ApiResult<?> findByLabels(List<Integer> labelIds, Integer page, Integer size) {
        return null;
    }

    /**
     * 帮助完成心愿
     *
     * @param vo 心愿信息
     * @return 完成结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> helpFinishWish(IWishHelpFinishVO vo) {

        if (vo.getAvatar() == null) {
            vo.setAvatar("");
        }
        if (vo.getContent() == null) {
            vo.setContent("");
        }
        IResourcesEntity resource = resourcesEntityRepository.findById(vo.getResourceId()).orElseThrow(() -> new RuntimeException("资源信息不存在"));
        IWishesEntity wish = wishesEntityRepository.findById(vo.getWishId()).orElseThrow(() -> new RuntimeException("心愿信息不存在"));
        // 字段校验后，进行保存
        IWishReplyListEntity entity = new IWishReplyListEntity();
        entity.setWishesEntity(wish);
        entity.setResourcesEntity(resource);
        entity.setPublishTime(LocalDateTime.now());
        entity.setAvatar(vo.getAvatar());
        entity.setContent(vo.getContent());
        entity.setUsername(vo.getUsername());
        wishReplyListEntityRepository.save(entity);
        return new ApiResult<>().success("帮助成功,谢谢你的贡献~");
    }

    /**
     * 通过内容和标签搜索
     *
     * @param content  内容
     * @param labelIds 标签ids
     * @param pageNum  页码
     * @param pageSize 页容量
     * @return 查询结果
     */
    @Override
    public ApiResult<?> findWishesByContentAndLabels(String content, List<Integer> labelIds, Integer pageNum, Integer pageSize) {
        List<ILabelsEntity> labels = labelsEntityRepository.findAllById(labelIds);
        Sort sort = Sort.by(Sort.Direction.DESC, "publishTime");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<IWishesEntity> page = null;
        if (!StringUtils.hasText(content)) {
            page = wishesEntityRepository.findAllByLabelsIn(labels, pageable);
        } else if (CollectionUtils.isEmpty(labelIds)) {
            content = "%" + content + "%";
            page = wishesEntityRepository.findAllByContentLikeIgnoreCase(content, pageable);
        } else {
            content = "%" + content + "%";
            page = wishesEntityRepository.findAllByContentLikeIgnoreCaseAndLabelsIn(content, labels, pageable);
        }
        PageData<IWishBaseInfoDTO> result = toResponseResult(page);
        return new ApiResult<PageData<IWishBaseInfoDTO>>().success(result);
    }
}
