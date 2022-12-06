package com.sheep.cloud.service.impl;

import com.sheep.cloud.aspect.CheckGoodsRelation;
import com.sheep.cloud.dao.sell.*;
import com.sheep.cloud.dto.request.sell.FindGoodsSortConditionParam;
import com.sheep.cloud.dto.request.sell.SaveOneGoodParam;
import com.sheep.cloud.dto.request.sell.UpdateGoodsInfoParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.PageData;
import com.sheep.cloud.dto.response.sell.IGoodsEntityBaseInfoDTO;
import com.sheep.cloud.dto.response.sell.IGoodsSimpleInfoDTO;
import com.sheep.cloud.entity.sell.ISellGoodsEntity;
import com.sheep.cloud.entity.sell.ISellGoodsTypeEntity;
import com.sheep.cloud.entity.sell.ISellUserEntity;
import com.sheep.cloud.service.IGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    private ISellGoodsEntityRepository goodsEntityRepository;
    @Autowired
    private ISellGoodsTypeEntityRepository goodsTypeEntityRepository;
    @Autowired
    private ISellUserEntityRepository userEntityRepository;
    @Autowired
    private ISellImageEntityRepository imageEntityRepository;
    @Autowired
    private ISellOrdersEntityRepository ordersEntityRepository;
    @Autowired
    private ISellShoppingCartEntityRepository shoppingCartEntityRepository;
    @Autowired
    private ISellSpikeDetailsEntityRepository spikeDetailsEntityRepository;
    @Autowired
    private ISellWishBuyEntityRepository wishBuyEntityRepository;
    @Autowired
    private ModelMapper modelMapper;

    private final PageData.PageDataBuilder<IGoodsEntityBaseInfoDTO> builder = PageData.builder();

    /**
     * 随机抽取size个商品
     *
     * @param size 数量
     * @return 商品列表
     */
    @Override
    public ApiResult<?> randomFindGoods(Integer size) {
        List<IGoodsSimpleInfoDTO> list = goodsEntityRepository.randomFindGoods(size).stream()
                .map(goodsEntity -> modelMapper.map(goodsEntity, IGoodsSimpleInfoDTO.class))
                .collect(Collectors.toList());
        return new ApiResult<>().success("ok", list);
    }

    @Override
    public ApiResult<?> findAllGoodsByKeyWord(String keyWord, FindGoodsSortConditionParam conditionParam, Pageable pageable) {
        Page<ISellGoodsEntity> page;
        int flag = 0;
        flag += conditionParam.isPriceDesc() ? 1 : 0;
        flag += conditionParam.isPriceAsc() ? 1 : 0;
        flag += conditionParam.isHistory() ? 1 : 0;
        assert flag <= 1 : "条件最多选择一个";
        if (conditionParam.isPriceAsc()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "price"));
        }
        if (conditionParam.isPriceDesc()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "price"));
        }
        if (conditionParam.isHistory()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "releaseTime"));
        }
        page = goodsEntityRepository.findAllByKeyWord(keyWord, pageable);
        assert page != null : "错误！";
        List<IGoodsEntityBaseInfoDTO> dtoList = page.get()
                .map(item -> modelMapper.map(item, IGoodsEntityBaseInfoDTO.class))
                .collect(Collectors.toList());
        return new ApiResult<PageData<IGoodsEntityBaseInfoDTO>>().success(builder
                .totalNum(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .data(dtoList)
                .build()
        );
    }

    /**
     * 发布一个商品
     *
     * @param param 商品信息
     * @return 发布结果
     */
    @Override
    public ApiResult<?> saveOne(SaveOneGoodParam param) {
        ISellUserEntity releaseUser = userEntityRepository.findById(param.getReleaseUserId()).orElseThrow(() -> new RuntimeException("商品发布者不存在"));
        ISellGoodsTypeEntity typeEntity = goodsTypeEntityRepository.findById(param.getTypeId()).orElseThrow(() -> new RuntimeException("商品类型不存在"));
        if (!param.getIsDiscount()) {
            param.setDiscountPercent(null);
        } else {
            Double percent = param.getDiscountPercent();
            if (percent <= 0 || percent >= 1) {
                throw new RuntimeException("折扣率不合法");
            }
        }
        ISellGoodsEntity entity = ISellGoodsEntity.builder()
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
        return new ApiResult<>().success("发布成功");
    }

    /**
     * 软删除某个商品
     *
     * @param id 商品id
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> deleteOne(Integer id) {
        ISellGoodsEntity goods = goodsEntityRepository.findById(id).orElseThrow(() -> new RuntimeException("商品不存在"));
        // 判断是否有关联数据
        // 判断是否有图片关联，有的话则删除
        if (imageEntityRepository.existsByGoodId(id)) {
            imageEntityRepository.deleteAllByGoodId(id);
        }
        if (ordersEntityRepository.existsByGoodId(id)) {
            return new ApiResult<>().error("该商品存在订单记录，无法删除");
        }
        // 判断是否有购物车关联，有的话则删除
        if (shoppingCartEntityRepository.existsByGoodsId(id)) {
            shoppingCartEntityRepository.deleteAllByGoodsId(id);
        }
        // 判断是否有秒杀关联，有的话则删除失败
        if (spikeDetailsEntityRepository.existsByGoodsId(id)) {
            return new ApiResult<>().error("该商品存在秒杀活动，无法删除");
        }
        // 判断是否有求购关联，有的话则删除失败
        if (wishBuyEntityRepository.existsByGoodId(id)) {
            return new ApiResult<>().error("该商品存在求购记录，无法删除");
        }
        goods.setIsDeleted(true);
        goods.setDeleteAt(LocalDateTime.now());
        goodsEntityRepository.save(goods);
        return new ApiResult<>().success("删除成功");
    }

    /**
     * 获取某个商品详情
     *
     * @param goodsId 商品id
     * @return 商品详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> findGoodsDetail(Integer goodsId) {
        ISellGoodsEntity goods = goodsEntityRepository
                .findById(goodsId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        IGoodsEntityBaseInfoDTO goodsEntityDTO = new IGoodsEntityBaseInfoDTO();
        modelMapper.map(goods, goodsEntityDTO);
        return new ApiResult<IGoodsEntityBaseInfoDTO>().success(goodsEntityDTO);
    }

    /**
     * 分页获取所有商品
     *
     * @param pageable 分页信息
     * @return 商品列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> findAllGoods(Pageable pageable) {
        Page<ISellGoodsEntity> page = goodsEntityRepository.findAll(pageable);
        List<IGoodsEntityBaseInfoDTO> dtoList = page.get()
                .map(item -> modelMapper.map(item, IGoodsEntityBaseInfoDTO.class))
                .collect(Collectors.toList());
        return new ApiResult<PageData<IGoodsEntityBaseInfoDTO>>().success(builder
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
    public ApiResult<?> findAllGoodsByUserId(Pageable pageable, Integer userId) {
        Page<ISellGoodsEntity> page = goodsEntityRepository.findAllByReleaseUserId(pageable, userId);
        List<IGoodsEntityBaseInfoDTO> dtoList = page.get()
                .map(item -> modelMapper.map(item, IGoodsEntityBaseInfoDTO.class))
                .collect(Collectors.toList());

        return new ApiResult<PageData<IGoodsEntityBaseInfoDTO>>().success(builder
                .totalNum(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .data(dtoList)
                .build()
        );
    }

    /**
     * 更新商品信息
     *
     * @param param 商品信息
     * @return 更新结果
     */
    @CheckGoodsRelation
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> updateGoodsInfo(UpdateGoodsInfoParam param) {
        if (!StringUtils.hasText(param.getName())) {
            throw new RuntimeException("商品名称不能为空");
        }
        ISellGoodsTypeEntity goodsType = goodsTypeEntityRepository.findById(param.getTypeId())
                .orElseThrow(() -> new RuntimeException("商品类型不存在"));
        ISellGoodsEntity entity = goodsEntityRepository.getOne(param.getId());
        if (param.getFreeTotal() < 0) throw new RuntimeException("请选择合适的商品库存");
        if (param.getPrice() <= 0) throw new RuntimeException("请选择合适的商品价格");
        boolean isDiscount = param.getIsDiscount();
        boolean isDiscountSuit = param.getDiscountPercent() <= 0 || param.getDiscountPercent() >= 1;
        if (isDiscount && isDiscountSuit) throw new RuntimeException("请输入合适的折扣");

        entity.setType(goodsType);
        entity.setName(param.getName());
        entity.setDescription(param.getDescription());
        entity.setPrice(param.getPrice());
        entity.setFreeTotal(param.getFreeTotal());
        entity.setIsDiscount(param.getIsDiscount());
        entity.setDiscountPercent(param.getDiscountPercent());
        entity.setCover(param.getCover());

        goodsEntityRepository.save(entity);
        return new ApiResult<>().success("更新成功");
    }

}

