package com.sheep.cloud.service.impl;

import com.sheep.cloud.dto.request.SaveOneGoodParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.IGoodsEntityBaseInfoDTO;
import com.sheep.cloud.dto.response.PageData;
import com.sheep.cloud.model.IGoodsEntity;
import com.sheep.cloud.model.IGoodsTypeEntity;
import com.sheep.cloud.model.IUserEntity;
import com.sheep.cloud.repository.*;
import com.sheep.cloud.service.IGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service.impl
 * @datetime 2022/9/23 星期五
 */
@Service
@Slf4j
public class IGoodsServiceImpl implements IGoodsService {

    @Autowired
    private IGoodsEntityRepository goodsEntityRepository;

    @Autowired
    private IGoodsTypeEntityRepository goodsTypeEntityRepository;

    @Autowired
    private IUserEntityRepository userEntityRepository;

    @Autowired
    private IImageEntityRepository imageEntityRepository;

    @Autowired
    private IOrdersEntityRepository ordersEntityRepository;

    @Autowired
    private IShoppingCartEntityRepository shoppingCartEntityRepository;

    @Autowired
    private ISpikeDetailsEntityRepository spikeDetailsEntityRepository;

    @Autowired
    private IWishBuyEntityRepository wishBuyEntityRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final PageData.PageDataBuilder<IGoodsEntityBaseInfoDTO> builder = PageData.builder();


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

    /**
     * 软删除某个商品
     *
     * @param id 商品id
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult deleteOne(Integer id) {
        IGoodsEntity goods = goodsEntityRepository.findById(id).orElseThrow(() -> new RuntimeException("商品不存在"));
        // 判断是否有关联数据
        // 判断是否有图片关联，有的话则删除
        if (imageEntityRepository.existsByGoodId(id)) {
            imageEntityRepository.deleteAllByGoodId(id);
        }
        if (ordersEntityRepository.existsByGoodId(id)) {
            return ApiResult.error("该商品存在订单记录，无法删除");
        }
        // 判断是否有购物车关联，有的话则删除
        if (shoppingCartEntityRepository.existsByGoodsId(id)) {
            shoppingCartEntityRepository.deleteAllByGoodsId(id);
        }
        // 判断是否有秒杀关联，有的话则删除失败
        if (spikeDetailsEntityRepository.existsByGoodsId(id)) {
            return ApiResult.error("该商品存在秒杀活动，无法删除");
        }
        // 判断是否有求购关联，有的话则删除失败
        if (wishBuyEntityRepository.existsByGoodId(id)) {
            return ApiResult.error("该商品存在求购记录，无法删除");
        }
        goods.setIsDeleted(true);
        goods.setDeleteAt(LocalDateTime.now());
        goodsEntityRepository.save(goods);
        return ApiResult.success("删除成功");
    }

    /**
     * 获取某个商品详情
     *
     * @param goodsId 商品id
     * @return 商品详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult findGoodsDetail(Integer goodsId) {
        IGoodsEntity goods = goodsEntityRepository
                .findById(goodsId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        IGoodsEntityBaseInfoDTO goodsEntityDTO = new IGoodsEntityBaseInfoDTO();
        modelMapper.map(goods, goodsEntityDTO);
        return ApiResult.success(goodsEntityDTO);
    }

    /**
     * 分页获取所有商品
     *
     * @param pageable 分页信息
     * @return 商品列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult findAllGoods(Pageable pageable) {
        Page<IGoodsEntity> page = goodsEntityRepository.findAll(pageable);
        List<IGoodsEntityBaseInfoDTO> dtoList = page.get()
                .map(item -> modelMapper.map(item, IGoodsEntityBaseInfoDTO.class))
                .collect(Collectors.toList());
        return ApiResult.success(builder
                .totalNum(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .data(dtoList)
                .build()
        );
    }

    /**
     * 分页获取某个用户发布的商品
     *
     * @param pageable 分页信息
     * @param userId   用户id
     * @return 商品列表
     */
    @Override
    public ApiResult findAllGoodsByUserId(Pageable pageable, Integer userId) {
        Page<IGoodsEntity> page = goodsEntityRepository.findAllByReleaseUserId(pageable, userId);
        List<IGoodsEntityBaseInfoDTO> dtoList = page.get()
                .map(item -> modelMapper.map(item, IGoodsEntityBaseInfoDTO.class))
                .collect(Collectors.toList());

        return ApiResult.success(builder
                .totalNum(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .data(dtoList)
                .build()
        );
    }
}

