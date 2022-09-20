package com.sheep.cloud.repository;

import com.sheep.cloud.model.IShoppingCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IShoppingCartEntityRepository extends JpaRepository<IShoppingCartEntity, Integer> {
}