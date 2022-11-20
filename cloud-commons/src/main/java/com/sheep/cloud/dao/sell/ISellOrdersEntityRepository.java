package com.sheep.cloud.dao.sell;

import com.sheep.cloud.entity.sell.ISellOrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ISellOrdersEntityRepository extends JpaRepository<ISellOrdersEntity, Integer> {

    // 根据订单号查询订单信息
    Optional<ISellOrdersEntity> findByOid(String orderId);

    Boolean existsByGoodId(Integer goodsId);
}
