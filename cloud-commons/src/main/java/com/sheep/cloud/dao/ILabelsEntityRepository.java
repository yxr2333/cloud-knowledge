package com.sheep.cloud.dao;

import com.sheep.cloud.entity.ILabelsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yxr
 */
public interface ILabelsEntityRepository extends JpaRepository<ILabelsEntity, Integer> {
    
}