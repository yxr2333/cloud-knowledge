package com.sheep.cloud.service.impl;

import com.sheep.cloud.dao.sell.ISellGoodsEntityRepository;
import com.sheep.cloud.dao.sell.ISellShoppingCartEntityRepository;
import com.sheep.cloud.dao.sell.ISellUserEntityRepository;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.entity.sell.ISellGoodsEntity;
import com.sheep.cloud.entity.sell.ISellShoppingCartEntity;
import com.sheep.cloud.entity.sell.ISellUserEntity;
import com.sheep.cloud.service.IShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WTY2002
 */
@Service
@Slf4j
public class IShoppingCartServiceImpl implements IShoppingCartService {
    @Autowired
    private ISellGoodsEntityRepository goodsEntityRepository;

    @Autowired
    private ISellUserEntityRepository userEntityRepository;

    @Autowired
    private ISellShoppingCartEntityRepository shoppingCartEntityRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * 创建一个购物车
     *
     * @id 用户id
     * @return 添加结果
     */
    @Override
    public ApiResult<?> creatShoppingCart(Integer id) {
        ISellUserEntity iSellUserEntity = userEntityRepository.findById(id).orElseThrow(() -> new RuntimeException("用户不存在"));
        if (shoppingCartEntityRepository.existsByUserId(id)) {
            return new ApiResult<>().warning("请勿重复创建购物车！");
        }
        List<ISellGoodsEntity> iSellGoodsEntityList = new ArrayList<>();
        ISellShoppingCartEntity iSellShoppingCartEntity = ISellShoppingCartEntity.builder()
                .user(iSellUserEntity)
                .goods(iSellGoodsEntityList)
                .build();
        shoppingCartEntityRepository.save(iSellShoppingCartEntity);
        return new ApiResult<>().success("创建成功！");
    }

    /**
     * 删除购物车
     *
     * @id 购物车id
     * @return 添加结果
     */
    @Override
    public ApiResult<?> delShoppingCart(Integer id) {
        if (!shoppingCartEntityRepository.existsById(id)) {
            return new ApiResult<>().warning("购物车不存在！");
        }
        shoppingCartEntityRepository.deleteById(id);
        return new ApiResult<>().success("删除成功！");
    }

    /**
     * 添加商品
     *
     * @sid 购物车id
     * @gid 商品id
     * @return 添加结果
     */
    @Override
    public ApiResult<?> addOne(Integer sid, Integer gid) {
        ISellShoppingCartEntity iSellShoppingCartEntity = shoppingCartEntityRepository.findById(sid).orElseThrow(() -> new RuntimeException("购物车不存在!"));
        ISellGoodsEntity iSellGoodsEntity = goodsEntityRepository.findById(gid).orElseThrow(() -> new RuntimeException("商品不存在!"));
        List<ISellGoodsEntity> list = iSellShoppingCartEntity.getGoods();
        for(ISellGoodsEntity i : list) {
            if (i.getId() == iSellGoodsEntity.getId()) {
                return new ApiResult<>().warning("请勿重复添加商品！");
            }
        }
        list.add(iSellGoodsEntity);
        iSellShoppingCartEntity.setGoods(list);
        shoppingCartEntityRepository.save(iSellShoppingCartEntity);
        return new ApiResult<>().success("添加成功！");
    }

    /**
     * 删除商品
     *
     * @sid 购物车id
     * @gid 商品id
     * @return 删除结果
     */
    @Override
    public ApiResult<?> deleteOne(Integer sid, Integer gid) {
        ISellShoppingCartEntity iSellShoppingCartEntity = shoppingCartEntityRepository.findById(sid).orElseThrow(() -> new RuntimeException("购物车不存在!"));
        ISellGoodsEntity iSellGoodsEntity = goodsEntityRepository.findById(gid).orElseThrow(() -> new RuntimeException("商品不存在!"));
        List<ISellGoodsEntity> list = iSellShoppingCartEntity.getGoods();
        boolean flag = true;
        for(ISellGoodsEntity i : list) {
            if (i.getId() == iSellGoodsEntity.getId()) {
                flag = false;
            }
        }
        if (flag) {
            return new ApiResult<>().warning("请勿重复删除商品！");
        }
        list.remove(iSellGoodsEntity);
        iSellShoppingCartEntity.setGoods(list);
        shoppingCartEntityRepository.save(iSellShoppingCartEntity);
        return new ApiResult<>().success("删除成功！");
    }

    /**
     * 查询购物车所有商品
     *
     * @param id   用户id
     * @param limit  数据条目数
     * @param offset 数据偏移量
     * @return 商品列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> getAll(Integer id,  Integer limit, Integer offset) {
//        ISellUserEntity iSellUserEntity = userEntityRepository.findById(id).orElseThrow(() -> new RuntimeException("用户不存在"));
//        ISellShoppingCartEntity iSellShoppingCartEntity = shoppingCartEntityRepository.findByUser(iSellUserEntity);
//        List<ISellGoodsEntity> iSellGoodsEntityList = iSellShoppingCartEntity.getGoods();
//
//
////        Page<ISellGoodsEntity> page = (Page<ISellGoodsEntity>) iSellShoppingCartEntity.getGoods();
//        PageRequest pageable = PageRequest.of(pageNum, pageSize);
//        Page<ISellGoodsEntity> page = shoppingCartEntityRepository.findAllByUserId(id,pageable);
//        PageData.PageDataBuilder<ISellGoodsEntity> builder = PageData.builder();
//        return new ApiResult<PageData<ISellGoodsEntity>>().success(builder.totalPage(page.getTotalPages())
//                .totalNum(page.getTotalElements())
//                .data(page.getContent())
//                .build());

//        ISellUserEntity iSellUserEntity = userEntityRepository.findById(id).orElseThrow(() -> new RuntimeException("用户不存在"));
//        ISellShoppingCartEntity iSellShoppingCartEntity = shoppingCartEntityRepository.findByUser(iSellUserEntity);
//        List<ISellGoodsEntity> list1 = iSellShoppingCartEntity.getGoods();
//        return new ApiResult<List<ISellGoodsEntity>>().success(list1);

        if (!userEntityRepository.existsById(id)) {
            throw new RuntimeException("用户不存在");
        }
        List<ISellGoodsEntity> list = entityManager.createNamedQuery("findShoppingCartGoodsByUid", ISellGoodsEntity.class)
                .setParameter(1, id)
                .setParameter(2, limit)
                .setParameter(3, offset)
                .getResultList();
        return new ApiResult<List<ISellGoodsEntity>>().success(list);
    }

    /**
     * 清空购物车
     *
     * @id 购物车id
     * @return 清空结果
     */
    @Override
    public ApiResult<?> delAll(Integer id) {
        ISellShoppingCartEntity iSellShoppingCartEntity = shoppingCartEntityRepository.findById(id).orElseThrow(() -> new RuntimeException("购物车不存在!"));
        List<ISellGoodsEntity> list = iSellShoppingCartEntity.getGoods();
        if (list.isEmpty()) {
            return new ApiResult<>().warning("购物车已清空！");
        } else {
            list.clear();
        }
        iSellShoppingCartEntity.setGoods(list);
        shoppingCartEntityRepository.save(iSellShoppingCartEntity);
        return new ApiResult<>().success("清空成功！");
    }


}
