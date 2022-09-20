package com.sheep.cloud.repository;

import com.sheep.cloud.model.IGoodsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGoodsEntityRepository extends JpaRepository<IGoodsEntity, Integer> {
}