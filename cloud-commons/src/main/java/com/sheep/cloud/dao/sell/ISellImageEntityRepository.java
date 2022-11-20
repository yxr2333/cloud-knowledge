package com.sheep.cloud.dao.sell;

import com.sheep.cloud.entity.sell.ISellImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISellImageEntityRepository extends JpaRepository<ISellImageEntity, Integer> {

    Boolean existsByGoodId(Integer goodId);

    void deleteAllByGoodId(Integer goodId);
}