package com.sheep.cloud.repository;

import com.sheep.cloud.model.ISpikeDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sheep
 */
public interface ISpikeDetailsEntityRepository extends JpaRepository<ISpikeDetailsEntity, Integer> {

    boolean existsByGoodsId(Integer goodsId);

}