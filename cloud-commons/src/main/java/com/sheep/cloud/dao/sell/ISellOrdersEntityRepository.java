package com.sheep.cloud.dao.sell;

import com.sheep.cloud.entity.sell.ISellGoodsEntity;
import com.sheep.cloud.entity.sell.ISellOrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author sheep
 */
public interface ISellOrdersEntityRepository extends JpaRepository<ISellOrdersEntity, Integer> {

    /**
     * 根据订单号查询订单信息
     */
    Optional<ISellOrdersEntity> findByOid(String orderId);

    Boolean existsByGoodId(Integer goodsId);

    /**
     * @return List<ISellOrdersEntity>
     * @id buyer_id
     */
    List<ISellOrdersEntity> findAllByBuyerId(Integer id);


    List<ISellOrdersEntity> findAllBySellerId(Integer id);

    @Query(value = "select orders.good from ISellOrdersEntity orders where orders.seller.id = ?1")
    List<ISellGoodsEntity> findUserSellGoodsList(Integer userId);

    @Query(value = "select orders.good from ISellOrdersEntity orders where orders.buyer.id = ?1")
    List<ISellGoodsEntity> findUserBuyGoodsList(Integer userId);
}
