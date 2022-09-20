package com.sheep.cloud.repository;

import com.sheep.cloud.model.IOrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrdersEntityRepository extends JpaRepository<IOrdersEntity, Integer> {
}