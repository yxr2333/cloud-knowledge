package com.sheep.cloud.service.impl;

import com.sheep.cloud.dao.sell.ISellGoodsTypeEntityRepository;
import com.sheep.cloud.dto.request.sell.GoodsTypeInfoParam;
import com.sheep.cloud.dto.request.sell.GoodsTypeParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.PageData;
import com.sheep.cloud.entity.sell.ISellGoodsTypeEntity;
import com.sheep.cloud.service.IGoodsTypeService;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author WTY2002
 */
@Service
@Slf4j
public class IGoodsTypeServiceImpl implements IGoodsTypeService {
    @Autowired
    private ISellGoodsTypeEntityRepository iSellGoodsTypeEntityRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final PageData.PageDataBuilder<ISellGoodsTypeEntity> builder = PageData.builder();
    /**
     * 添加商品类别
     *
     * @param goodsTypeParam  商品标签信息
     * @return 添加结果
     */
    @Override
    public ApiResult<?> addIGoodsType(GoodsTypeParam goodsTypeParam) {
        if (StringUtil.isNullOrEmpty(goodsTypeParam.getName())) {
            return new ApiResult<>().warning("类别名称不允许为空！");
        }
        ISellGoodsTypeEntity iSellGoodsTypeEntity = new ISellGoodsTypeEntity();
        iSellGoodsTypeEntity.setName(goodsTypeParam.getName());
        if (goodsTypeParam.getFid() == null) {
            iSellGoodsTypeEntity.setTypeLevel(1);
        } else if (!iSellGoodsTypeEntityRepository.existsById(goodsTypeParam.getFid())){
            return new ApiResult<>().warning("父级标签不存在！");
        } else {
            iSellGoodsTypeEntity.setTypeLevel(iSellGoodsTypeEntityRepository.getOne(goodsTypeParam.getFid())
                    .getTypeLevel() + 1);
            iSellGoodsTypeEntity.setParent(iSellGoodsTypeEntityRepository.getOne(goodsTypeParam.getFid()));
        }
        iSellGoodsTypeEntityRepository.save(iSellGoodsTypeEntity);
        return new ApiResult<>().success("添加成功！");
    }


    /**
     * 删除商品类别
     *
     * @param id  商品标签id
     * @return 删除结果
     */
    @Override
    public ApiResult<?> delIGoodsType(Integer id) {
        if (id == null) {
            return new ApiResult<>().warning("商品id不能为空！");
        }
        if (!iSellGoodsTypeEntityRepository.existsById(id)) {
            return new ApiResult<>().warning("商品不存在");
        }
        List<ISellGoodsTypeEntity> list = iSellGoodsTypeEntityRepository.findAll();
        for (ISellGoodsTypeEntity t : list) {
            if (t.getParent() != null && t.getParent().getId() == id) {
                return new ApiResult<>().warning("该标签存在子标签，不允许删除！");
            }
        }
        iSellGoodsTypeEntityRepository.deleteById(id);
        return new ApiResult<>().success("删除成功！");
    }

    /**
     * 修改商品类别
     *
     * @param goodsTypeInfoParam  商品标签信息
     * @return 修改结果
     */
    @Override
    public ApiResult<?> modifyIGoodsType(GoodsTypeInfoParam goodsTypeInfoParam) {
        if (goodsTypeInfoParam.getId() == null) {
            return new ApiResult<>().warning("商品类别id不允许为空！");
        }
        if (StringUtil.isNullOrEmpty(goodsTypeInfoParam.getName())) {
            return new ApiResult<>().warning("类别名称不允许为空！");
        }
        ISellGoodsTypeEntity iSellGoodsTypeEntity = new ISellGoodsTypeEntity();
        iSellGoodsTypeEntity.setId(goodsTypeInfoParam.getId());
        iSellGoodsTypeEntity.setName(goodsTypeInfoParam.getName());
        if (!iSellGoodsTypeEntityRepository.existsById(goodsTypeInfoParam.getFid())) {
            return new ApiResult<>().warning("父级标签不存在！");
        } else {
            if (goodsTypeInfoParam.getFid() == null) {
                iSellGoodsTypeEntity.setTypeLevel(1);
            } else {
                iSellGoodsTypeEntity.setTypeLevel(iSellGoodsTypeEntityRepository.getOne(goodsTypeInfoParam.getFid()).getTypeLevel() + 1);
                iSellGoodsTypeEntity.setParent(iSellGoodsTypeEntityRepository.getOne(goodsTypeInfoParam.getFid()));
            }
            List<ISellGoodsTypeEntity> list = iSellGoodsTypeEntityRepository.findAll();
            for (ISellGoodsTypeEntity t : list) {
                if (t.getParent() != null && t.getParent().getId() == goodsTypeInfoParam.getId()) {
                    t.setTypeLevel(iSellGoodsTypeEntity.getTypeLevel() + 1);
                    iSellGoodsTypeEntityRepository.save(t);
                }
            }
        }
        iSellGoodsTypeEntityRepository.save(iSellGoodsTypeEntity);
        return new ApiResult<>().success("修改成功！");
    }

    /**
     * 分页查询所有商品类别
     *
     * @param pageable 分页信息
     * @return 查询结果
     */
    @Override
    public ApiResult<?> getAllIGoodsType(Pageable pageable) {
        Page<ISellGoodsTypeEntity> page = iSellGoodsTypeEntityRepository.findAll(pageable);
        List<ISellGoodsTypeEntity> dtoList = page.get()
                .map(item -> modelMapper.map(item, ISellGoodsTypeEntity.class))
                .collect(Collectors.toList());
        return new ApiResult<PageData<ISellGoodsTypeEntity>>().success(builder
                .totalNum(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .data(dtoList)
                .build()
        );
    }

    /**
     * 查询单个商品类别
     *
     * @param id  商品标签id
     * @return 查询结果
     */
    @Override
    public ApiResult<?> getOneIGoodsType(Integer id) {
        ISellGoodsTypeEntity iSellGoodsTypeEntity = new ISellGoodsTypeEntity();
        try {
            iSellGoodsTypeEntity = iSellGoodsTypeEntityRepository.getOne(id);
        } catch (Exception e) {
            throw new RuntimeException("商品类别不存在!");
        }
        return new ApiResult<>().success(iSellGoodsTypeEntity);
    }
}
