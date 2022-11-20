package com.sheep.cloud.service.impl;

import com.sheep.cloud.common.CommonFields;
import com.sheep.cloud.dto.request.sell.PublishWishBuyEntityParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.sell.IWishBuyEntityBaseInfoDTO;
import com.sheep.cloud.entity.sell.ISellGoodsTypeEntity;
import com.sheep.cloud.entity.sell.ISellUserEntity;
import com.sheep.cloud.entity.sell.ISellWishBuyEntity;
import com.sheep.cloud.dao.sell.ISellGoodsTypeEntityRepository;
import com.sheep.cloud.dao.sell.ISellUserEntityRepository;
import com.sheep.cloud.dao.sell.ISellWishBuyEntityRepository;
import com.sheep.cloud.service.WishBuyService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发布一条新的求购信息
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

    /**
     * 根据id删除求购信息
     *
     * @param id 求购信息id
     * @return 删除结果
     */
    @Override
    public ApiResult deleteOne(Integer id) {
        return null;
    }
}
