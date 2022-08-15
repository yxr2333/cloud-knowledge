package com.sheep.cloud.dao;

import com.sheep.cloud.entity.ILabelCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author yxr
 */
public interface ILabelCategoryEntityRepository extends JpaRepository<ILabelCategoryEntity, Integer>, JpaSpecificationExecutor<ILabelCategoryEntity> {
}