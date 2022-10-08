package com.sheep.cloud.repository;

import com.sheep.cloud.model.IOrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IOrdersEntityRepository extends JpaRepository<IOrdersEntity, Integer> {

    // 根据订单号查询订单信息
    Optional<IOrdersEntity> findByOid(String orderId);
}