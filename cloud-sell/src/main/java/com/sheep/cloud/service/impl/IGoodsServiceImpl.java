package com.sheep.cloud.service.impl;

import com.sheep.cloud.dto.request.SaveOneGoodParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.model.IGoodsEntity;
import com.sheep.cloud.model.IGoodsTypeEntity;
import com.sheep.cloud.model.IUserEntity;
import com.sheep.cloud.repository.IGoodsEntityRepository;
import com.sheep.cloud.repository.IGoodsTypeEntityRepository;
import com.sheep.cloud.repository.IUserEntityRepository;
import com.sheep.cloud.service.IGoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service.impl
 * @datetime 2022/9/23 星期五
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class IGoodsServiceImpl implements IGoodsService {

    private final IGoodsEntityRepository goodsEntityRepository;

    private final IGoodsTypeEntityRepository goodsTypeEntityRepository;

    private final IUserEntityRepository userEntityRepository;

    /**
     * 发布一个商品
     *
     * @param param 商品信息
     * @return 发布结果
     */
    @Override
    public ApiResult saveOne(SaveOneGoodParam param) {
        IUserEntity releaseUser = userEntityRepository.findById(param.getReleaseUserId()).orElseThrow(() -> new RuntimeException("商品发布者不存在"));
        IGoodsTypeEntity typeEntity = goodsTypeEntityRepository.findById(param.getTypeId()).orElseThrow(() -> new RuntimeException("商品类型不存在"));
        if (!param.getIsDiscount()) {
            param.setDiscountPercent(null);
        } else {
            Double percent = param.getDiscountPercent();
            if (percent <= 0 || percent >= 1) {
                throw new RuntimeException("折扣率不合法");
            }
        }
        
        IGoodsEntity entity = IGoodsEntity.builder()
                .name(param.getGoodsName())
                .description(param.getDescription())
                .price(param.getPrice())
                .brand(param.getBrand())
                .type(typeEntity)
                .freeTotal(param.getFreeTotal())
                .cover(param.getCover())
                .releaseUser(releaseUser)
                .isDiscount(param.getIsDiscount())
                .discountPercent(param.getDiscountPercent())
                .releaseTime(LocalDateTime.now()).build();

        goodsEntityRepository.save(entity);
        return ApiResult.success("发布成功");
    }
}

