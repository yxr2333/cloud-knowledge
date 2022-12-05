package com.sheep.cloud.dao.sell;

import com.sheep.cloud.entity.sell.ISellWishBuyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ISellWishBuyEntityRepository extends JpaRepository<ISellWishBuyEntity, Integer> , JpaSpecificationExecutor<ISellWishBuyEntity> {

    boolean existsByGoodId(Integer goodsId);

    //@Query("select distinct w from ISellWishBuyEntity w where w.pubUser.id=?1")
    List<ISellWishBuyEntity> findAllByPubUserId(Integer id);
}