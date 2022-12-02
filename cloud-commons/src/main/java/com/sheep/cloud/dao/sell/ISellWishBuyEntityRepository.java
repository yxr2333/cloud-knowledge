package com.sheep.cloud.dao.sell;

import com.sheep.cloud.entity.sell.ISellWishBuyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ISellWishBuyEntityRepository extends
        JpaRepository<ISellWishBuyEntity, Integer>,
        JpaSpecificationExecutor<ISellWishBuyEntity> {

    boolean existsByGoodId(Integer goodsId);

    Page<ISellWishBuyEntity> findAllByTypeId(Integer typeId, Pageable pageable);
}