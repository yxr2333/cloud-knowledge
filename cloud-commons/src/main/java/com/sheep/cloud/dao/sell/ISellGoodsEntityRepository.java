package com.sheep.cloud.dao.sell;

import com.sheep.cloud.entity.sell.ISellGoodsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ISellGoodsEntityRepository extends JpaRepository<ISellGoodsEntity, Integer>, JpaSpecificationExecutor<ISellGoodsEntity> {

    Page<ISellGoodsEntity> findAllByReleaseUserId(Pageable pageable, Integer uid);

    List<ISellGoodsEntity> findAllByReleaseUserId(Integer id);

}