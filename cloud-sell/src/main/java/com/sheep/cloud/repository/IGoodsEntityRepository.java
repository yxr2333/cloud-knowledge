package com.sheep.cloud.repository;

import com.sheep.cloud.model.IGoodsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IGoodsEntityRepository extends JpaRepository<IGoodsEntity, Integer>, JpaSpecificationExecutor<IGoodsEntity> {

    Page<IGoodsEntity> findAllByReleaseUserId(Pageable pageable, Integer uid);
}