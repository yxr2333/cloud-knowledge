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
 * @datetime 2022/10/10 ζζδΈ
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
     * εεΈδΈζ‘ζ°ε°ζ±θ΄­δΏ‘ζ―
     *
     * @param param ζ±θ΄­δΏ‘ζ―
     * @return εεΈη»ζ
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> publishOne(PublishWishBuyEntityParam param) {
        ISellGoodsTypeEntity type = goodsTypeEntityRepository.findById(param.getTypeId())
                .orElseThrow(() -> new RuntimeException("δΈζ―ζηεεη±»ε"));
        ISellUserEntity pubUser = userEntityRepository.findById(param.getPubUserId())
                .orElseThrow(() -> new RuntimeException("ζͺζΎε°εεΈθδΏ‘ζ―"));
        ISellWishBuyEntity entity = new ISellWishBuyEntity()
                .setDescription(param.getDescription())
                .setPubUser(pubUser)
                .setType(type)
                .setImgUrl(param.getImgUrl());
        ISellWishBuyEntity wishBuy = wishBuyEntityRepository.save(entity);
        log.info("εεΈζ±θ΄­δΏ‘ζ―ζεοΌηΌε·δΈΊοΌ{}", wishBuy.getId());
        rabbitTemplate.convertAndSend(CommonFields.WISH_BUY_EXCHANGE_NAME, CommonFields.WISH_BUY_ROUTING_KEY, String.valueOf(wishBuy.getId()), message -> {
            // θ?Ύη½?ε»ΆζΆ10s
            message.getMessageProperties().setDelay(10 * 1000);
            log.info("εε»ΆζΆιεειζΆζ―οΌζ±θ΄­δΏ‘ζ―ηΌε·οΌ{}", wishBuy.getId());
            return message;
        });
        return new ApiResult<>().success("εεΈζεοΌθ―·εεΎι¦ι‘΅ζ₯η");
    }

    /**
     * ζ Ήζ?idθ·εζ±θ΄­δΏ‘ζ―
     *
     * @param id ζ±θ΄­δΏ‘ζ―id
     * @return ζ±θ΄­δΏ‘ζ―
     */
    @Override
    public ApiResult<?> findWishBuyDetail(Integer id) {
        ISellWishBuyEntity entity = wishBuyEntityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ζͺζΎε°ζ±θ΄­δΏ‘ζ―"));
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
     * ζ Ήζ?idε ι€ζ±θ΄­δΏ‘ζ―
     *
     * @param ids ζ±θ΄­δΏ‘ζ―idιε
     * @return ε ι€η»ζ
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> deleteMultiple(List<Integer> ids) {
        for (Integer id : ids) {
            if (id == null) {
                throw new RuntimeException("idδΈθ½δΈΊη©Ί");
            }
            wishBuyEntityRepository.findById(id).orElseThrow(() -> new RuntimeException("ζ±θ΄­δΏ‘ζ―δΈε­ε¨"));
            wishBuyEntityRepository.deleteById(id);
        }
        return new ApiResult<>().success("ε ι€ζε");
    }

    /**
     * ζ Ήζ?idζ΄ζ°ζ±θ΄­δΏ‘ζ―
     *
     * @param param δΏ?ζΉζ±θ΄­δΏ‘ζ―
     * @return δΏ?ζΉη»ζ
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> updateWishBuyDetail(UpdateWishBuyInfoParam param) {
        wishBuyEntityRepository.findById(param.getId()).orElseThrow(() -> new RuntimeException("ζ±θ΄­δΏ‘ζ―δΈε­ε¨"));
        ISellGoodsTypeEntity type = goodsTypeEntityRepository.findById(param.getTypeId())
                .orElseThrow(() -> new RuntimeException("δΈζ―ζηεεη±»ε"));
        ISellWishBuyEntity wishBuy = wishBuyEntityRepository
                .save(new ISellWishBuyEntity()
                        .setId(param.getId())
                        .setDescription(param.getDescription())
                        .setType(type)
                        .setImgUrl(param.getImgUrl()));
        log.info("δΏ?ζΉζ±θ΄­δΏ‘ζ―ζεοΌηΌε·δΈΊοΌ{}", wishBuy.getId());
        return new ApiResult<>().success("δΏ?ζΉζεοΌ");
    }

}
