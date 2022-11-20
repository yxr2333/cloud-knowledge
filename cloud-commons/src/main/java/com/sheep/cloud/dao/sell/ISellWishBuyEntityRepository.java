package com.sheep.cloud.dao.sell;

import com.sheep.cloud.entity.sell.ISellWishBuyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISellWishBuyEntityRepository extends JpaRepository<ISellWishBuyEntity, Integer> {

    boolean existsByGoodId(Integer goodsId);
}