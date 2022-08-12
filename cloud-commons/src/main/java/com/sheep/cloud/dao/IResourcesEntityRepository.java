package com.sheep.cloud.dao;

import com.sheep.cloud.entity.IResourcesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yxr
 */
public interface IResourcesEntityRepository extends JpaRepository<IResourcesEntity, Integer> {
}