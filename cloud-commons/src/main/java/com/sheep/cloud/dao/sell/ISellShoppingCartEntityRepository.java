package com.sheep.cloud.dao.sell;

import com.sheep.cloud.entity.sell.ISellShoppingCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISellShoppingCartEntityRepository extends JpaRepository<ISellShoppingCartEntity, Integer> {

    boolean existsByGoodsId(Integer goodsId);

    void deleteAllByGoodsId(Integer goodsId);
}