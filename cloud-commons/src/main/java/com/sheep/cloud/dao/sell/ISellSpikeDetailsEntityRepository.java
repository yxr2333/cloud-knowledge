package com.sheep.cloud.dao.sell;

import com.sheep.cloud.entity.sell.ISellSpikeDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author sheep
 */
public interface ISellSpikeDetailsEntityRepository extends JpaRepository<ISellSpikeDetailsEntity, Integer> {

    boolean existsByGoodsId(Integer goodsId);

    List<ISellSpikeDetailsEntity> findAllBySpikeId(Integer spikeId);
}