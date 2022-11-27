package com.sheep.cloud.dao.sell;

import com.sheep.cloud.entity.sell.ISellGoodsEntity;
import com.sheep.cloud.entity.sell.ISellShoppingCartEntity;
import com.sheep.cloud.entity.sell.ISellUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ISellShoppingCartEntityRepository extends JpaRepository<ISellShoppingCartEntity, Integer> {

    boolean existsByGoodsId(Integer goodsId);

    void deleteAllByGoodsId(Integer goodsId);

    ISellShoppingCartEntity findByUser(ISellUserEntity user);

    boolean existsByUserId(Integer id);

    @Query("select i from ISellShoppingCartEntity i where i.user.id = ?1")
    Page<ISellGoodsEntity> findAllByUserId(Integer id, Pageable pageable);
}