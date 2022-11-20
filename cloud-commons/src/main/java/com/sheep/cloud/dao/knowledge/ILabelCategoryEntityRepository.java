package com.sheep.cloud.dao.knowledge;

import com.sheep.cloud.entity.knowledge.ILabelCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author yxr
 */
public interface ILabelCategoryEntityRepository extends JpaRepository<ILabelCategoryEntity, Integer>, JpaSpecificationExecutor<ILabelCategoryEntity> {
}