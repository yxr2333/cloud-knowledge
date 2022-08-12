package com.sheep.cloud.dao;

import com.sheep.cloud.entity.IWishesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yxr
 */
public interface IWishesEntityRepository extends JpaRepository<IWishesEntity, Integer> {
}