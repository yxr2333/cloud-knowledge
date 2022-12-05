package com.sheep.cloud.service.impl;

import com.sheep.cloud.common.CommonFields;
import com.sheep.cloud.dao.sell.ISellGoodsTypeEntityRepository;
import com.sheep.cloud.dao.sell.ISellUserEntityRepository;
import com.sheep.cloud.dao.sell.ISellWishBuyEntityRepository;
import com.sheep.cloud.dto.request.sell.FindWishBuyConditionParam;
import com.sheep.cloud.dto.request.sell.PublishWishBuyEntityParam;
import com.sheep.cloud.dto.request.sell.UpdateWishBuyInfoParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.PageData;
import com.sheep.cloud.dto.response.sell.IWishBuyEntityBaseInfoDTO;
import com.sheep.cloud.entity.sell.ISellGoodsTypeEntity;
import com.sheep.cloud.entity.sell.ISellUserEntity;
import com.sheep.cloud.entity.sell.ISellWishBuyEntity;
import com.sheep.cloud.service.WishBuyService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service.impl
 * @datetime 2022/10/10 星期一
 */
@Service
@Slf4j
public class WishBuyServiceImpl implements WishBuyService {

    @Autowired
    private ISellWishBuyEntityRepository wishBuyEntityRepository;

    @Autowired
    private ISellUserEntityRepository userEntityRepository;

    @Autowired
    private ISellGoodsTypeEntityRepository goodsTypeEntityRepository;

    private final PageData.PageDataBuilder<ISellWishBuyEntity> builder = PageData.builder();

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发布一条新地求购信息
     *
     * @param param 求购信息
     * @return 发布结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> publishOne(PublishWishBuyEntityParam param) {
        ISellGoodsTypeEntity type = goodsTypeEntityRepository.findById(param.getTypeId())
                .orElseThrow(() -> new RuntimeException("不支持的商品类型"));
        ISellUserEntity pubUser = userEntityRepository.findById(param.getPubUserId())
                .orElseThrow(() -> new RuntimeException("未找到发布者信息"));
        ISellWishBuyEntity entity = new ISellWishBuyEntity()
                .setDescription(param.getDescription())
                .setPubUser(pubUser)
                .setType(type)
                .setImgUrl(param.getImgUrl());
        ISellWishBuyEntity wishBuy = wishBuyEntityRepository.save(entity);
        log.info("发布求购信息成功！编号为：{}", wishBuy.getId());
        rabbitTemplate.convertAndSend(CommonFields.WISH_BUY_EXCHANGE_NAME, CommonFields.WISH_BUY_ROUTING_KEY, String.valueOf(wishBuy.getId()), message -> {
            // 设置延时10s
            message.getMessageProperties().setDelay(10 * 1000);
            log.info("向延时队列发送消息，求购信息编号：{}", wishBuy.getId());
            return message;
        });
        return new ApiResult<>().success("发布成功！请前往首页查看");
    }

    /**
     * 根据id获取求购信息
     *
     * @param id 求购信息id
     * @return 求购信息
     */
    @Override
    public ApiResult<?> findWishBuyDetail(Integer id) {
        ISellWishBuyEntity entity = wishBuyEntityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("未找到求购信息"));
        IWishBuyEntityBaseInfoDTO baseInfoDTO = modelMapper.map(entity, IWishBuyEntityBaseInfoDTO.class);
        return new ApiResult<>().success(baseInfoDTO);
    }

    @Override
    public ApiResult<?> findWishBuyDetailConditionally(Pageable pageable, FindWishBuyConditionParam queryParam) {
        Specification<ISellWishBuyEntity> specification =
                (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    predicates.add(criteriaBuilder.like(root.get("goodName"), queryParam.getGoodName()));
                    if (queryParam.getType() != null) {
                        predicates.add(criteriaBuilder.equal(root.get("type"), queryParam.getType()));
                    }
                    if (queryParam.getDescription() != null) {
                        predicates.add(criteriaBuilder.equal(root.get("describe"), queryParam.getDescription()));
                    }
                    if (queryParam.getType() != null) {
                        ISellGoodsTypeEntity goodsType = goodsTypeEntityRepository.findByName(queryParam.getType());
                        predicates.add(criteriaBuilder.equal(root.get("type"), goodsType.getId()));
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                };
        Page<ISellWishBuyEntity> page = wishBuyEntityRepository.findAll(specification, pageable);
        List<ISellWishBuyEntity> dtoList = page.get()
                .map(item -> modelMapper.map(item, ISellWishBuyEntity.class))
                .collect(Collectors.toList());
        return new ApiResult<PageData<ISellWishBuyEntity>>().success(builder
                .totalNum(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .data(dtoList)
                .build());
    }

    /**
     * 根据id删除求购信息
     *
     * @param ids 求购信息id队列
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> deleteMultiple(List<Integer> ids) {
        for (Integer id : ids) {
            if (id == null) {
                throw new RuntimeException("id不能为空");
            }
            wishBuyEntityRepository.findById(id).orElseThrow(() -> new RuntimeException("求购信息不存在"));
            wishBuyEntityRepository.deleteById(id);
        }
        return new ApiResult<>().success("删除成功");
    }

    /**
     * 根据id更新求购信息
     *
     * @param param 修改求购信息
     * @return 修改结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> updateWishBuyDetail(UpdateWishBuyInfoParam param) {
        wishBuyEntityRepository.findById(param.getId()).orElseThrow(() -> new RuntimeException("求购信息不存在"));
        ISellGoodsTypeEntity type = goodsTypeEntityRepository.findById(param.getTypeId())
                .orElseThrow(() -> new RuntimeException("不支持的商品类型"));
        ISellWishBuyEntity wishBuy = wishBuyEntityRepository
                .save(new ISellWishBuyEntity()
                        .setId(param.getId())
                        .setDescription(param.getDescription())
                        .setType(type)
                        .setImgUrl(param.getImgUrl()));
        log.info("修改求购信息成功！编号为：{}", wishBuy.getId());
        return new ApiResult<>().success("修改成功！");
    }

}
