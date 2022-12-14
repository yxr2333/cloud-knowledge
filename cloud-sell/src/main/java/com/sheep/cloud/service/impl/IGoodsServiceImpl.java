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
 * @datetime 2022/9/23 ζζδΊ
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
     * ιζΊζ½εsizeδΈͺεε
     *
     * @param size ζ°ι
     * @return εεεθ‘¨
     */
    @Override
    public ApiResult<?> randomFindGoods(Integer size) {
        List<IGoodsSimpleInfoDTO> list = goodsEntityRepository.randomFindGoods(size).stream()
                .map(goodsEntity -> modelMapper.map(goodsEntity, IGoodsSimpleInfoDTO.class))
                .collect(Collectors.toList());
        return new ApiResult<>().success("ok", list);
    }

    /**
     * ζ₯θ―’ζδΈͺεεη±»ε«δΈηζζεε
     *
     * @param typeId   η±»ε«id
     * @param pageable ει‘΅δΏ‘ζ―
     * @return ζ₯θ―’η»ζ
     */
    @Override
    public ApiResult<?> findAllGoodsByType(Integer typeId, Pageable pageable) {
        goodsTypeEntityRepository.findById(typeId).orElseThrow(() -> new RuntimeException("εεη±»ε«δΈε­ε¨"));
        Page<ISellGoodsEntity> page = goodsEntityRepository.findAllByTypeId(typeId, pageable);
        List<IGoodsEntityBaseInfoDTO> list = page.getContent().stream()
                .map(goodsEntity -> modelMapper.map(goodsEntity, IGoodsEntityBaseInfoDTO.class))
                .collect(Collectors.toList());
        PageData<IGoodsEntityBaseInfoDTO> pageData = builder.data(list)
                .totalNum(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();
        return new ApiResult<>().success("ok", pageData);
    }

    @Override
    public ApiResult<?> findAllGoodsByKeyWord(String keyWord, FindGoodsSortConditionParam conditionParam, Pageable pageable) {
        Page<ISellGoodsEntity> page;
        int flag = 0;
        flag += conditionParam.isPriceDesc() ? 1 : 0;
        flag += conditionParam.isPriceAsc() ? 1 : 0;
        flag += conditionParam.isHistory() ? 1 : 0;
        assert flag <= 1 : "ζ‘δ»Άζε€ιζ©δΈδΈͺ";
        System.out.println(flag);
        System.out.println(conditionParam);
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
        assert page != null : "ιθ――οΌ";
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
     * εεΈδΈδΈͺεε
     *
     * @param param εεδΏ‘ζ―
     * @return εεΈη»ζ
     */
    @Override
    public ApiResult<?> saveOne(SaveOneGoodParam param) {
        ISellUserEntity releaseUser = userEntityRepository.findById(param.getReleaseUserId()).orElseThrow(() -> new RuntimeException("εεεεΈθδΈε­ε¨"));
        ISellGoodsTypeEntity typeEntity = goodsTypeEntityRepository.findById(param.getTypeId()).orElseThrow(() -> new RuntimeException("εεη±»εδΈε­ε¨"));
        if (!param.getIsDiscount()) {
            param.setDiscountPercent(null);
        } else {
            Double percent = param.getDiscountPercent();
            if (percent <= 0 || percent >= 1) {
                throw new RuntimeException("ζζ£ηδΈεζ³");
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
        return new ApiResult<>().success("εεΈζε");
    }

    /**
     * θ½―ε ι€ζδΈͺεε
     *
     * @param id εεid
     * @return ε ι€η»ζ
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> deleteOne(Integer id) {
        ISellGoodsEntity goods = goodsEntityRepository.findById(id).orElseThrow(() -> new RuntimeException("εεδΈε­ε¨"));
        // ε€ζ­ζ―ε¦ζε³θζ°ζ?
        // ε€ζ­ζ―ε¦ζεΎηε³θοΌζηθ―εε ι€
        if (imageEntityRepository.existsByGoodId(id)) {
            imageEntityRepository.deleteAllByGoodId(id);
        }
        if (ordersEntityRepository.existsByGoodId(id)) {
            return new ApiResult<>().error("θ―₯εεε­ε¨θ?’εθ?°ε½οΌζ ζ³ε ι€");
        }
        // ε€ζ­ζ―ε¦ζθ΄­η©θ½¦ε³θοΌζηθ―εε ι€
        if (shoppingCartEntityRepository.existsByGoodsId(id)) {
            shoppingCartEntityRepository.deleteAllByGoodsId(id);
        }
        // ε€ζ­ζ―ε¦ζη§ζε³θοΌζηθ―εε ι€ε€±θ΄₯
        if (spikeDetailsEntityRepository.existsByGoodsId(id)) {
            return new ApiResult<>().error("θ―₯εεε­ε¨η§ζζ΄»ε¨οΌζ ζ³ε ι€");
        }
        // ε€ζ­ζ―ε¦ζζ±θ΄­ε³θοΌζηθ―εε ι€ε€±θ΄₯
        if (wishBuyEntityRepository.existsByGoodId(id)) {
            return new ApiResult<>().error("θ―₯εεε­ε¨ζ±θ΄­θ?°ε½οΌζ ζ³ε ι€");
        }
        goods.setIsDeleted(true);
        goods.setDeleteAt(LocalDateTime.now());
        goodsEntityRepository.save(goods);
        return new ApiResult<>().success("ε ι€ζε");
    }

    /**
     * θ·εζδΈͺεεθ―¦ζ
     *
     * @param goodsId εεid
     * @return εεθ―¦ζ
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> findGoodsDetail(Integer goodsId) {
        ISellGoodsEntity goods = goodsEntityRepository
                .findById(goodsId)
                .orElseThrow(() -> new RuntimeException("εεδΈε­ε¨"));
        IGoodsEntityBaseInfoDTO goodsEntityDTO = new IGoodsEntityBaseInfoDTO();
        modelMapper.map(goods, goodsEntityDTO);
        return new ApiResult<IGoodsEntityBaseInfoDTO>().success(goodsEntityDTO);
    }

    /**
     * ει‘΅θ·εζζεε
     *
     * @param pageable ει‘΅δΏ‘ζ―
     * @return εεεθ‘¨
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
     * ει‘΅θ·εζδΈͺη¨ζ·εεΈηεε
     *
     * @param pageable ει‘΅δΏ‘ζ―
     * @param userId   η¨ζ·id
     * @return εεεθ‘¨
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
     * ζ΄ζ°εεδΏ‘ζ―
     *
     * @param param εεδΏ‘ζ―
     * @return ζ΄ζ°η»ζ
     */
    @CheckGoodsRelation
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> updateGoodsInfo(UpdateGoodsInfoParam param) {
        if (!StringUtils.hasText(param.getName())) {
            throw new RuntimeException("εεεη§°δΈθ½δΈΊη©Ί");
        }
        ISellGoodsTypeEntity goodsType = goodsTypeEntityRepository.findById(param.getTypeId())
                .orElseThrow(() -> new RuntimeException("εεη±»εδΈε­ε¨"));
        ISellGoodsEntity entity = goodsEntityRepository.getOne(param.getId());
        if (param.getFreeTotal() < 0) throw new RuntimeException("θ―·ιζ©ειηεεεΊε­");
        if (param.getPrice() <= 0) throw new RuntimeException("θ―·ιζ©ειηεεδ»·ζ Ό");
        boolean isDiscount = param.getIsDiscount();
        boolean isDiscountSuit = param.getDiscountPercent() <= 0 || param.getDiscountPercent() >= 1;
        if (isDiscount && isDiscountSuit) throw new RuntimeException("θ―·θΎε₯ειηζζ£");

        entity.setType(goodsType);
        entity.setName(param.getName());
        entity.setDescription(param.getDescription());
        entity.setPrice(param.getPrice());
        entity.setFreeTotal(param.getFreeTotal());
        entity.setIsDiscount(param.getIsDiscount());
        entity.setDiscountPercent(param.getDiscountPercent());
        entity.setCover(param.getCover());

        goodsEntityRepository.save(entity);
        return new ApiResult<>().success("ζ΄ζ°ζε");
    }

}

