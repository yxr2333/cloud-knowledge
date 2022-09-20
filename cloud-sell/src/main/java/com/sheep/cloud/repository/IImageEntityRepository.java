package com.sheep.cloud.repository;

import com.sheep.cloud.model.IImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IImageEntityRepository extends JpaRepository<IImageEntity, Integer> {
}