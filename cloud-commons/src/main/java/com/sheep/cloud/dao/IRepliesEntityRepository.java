package com.sheep.cloud.dao;

import com.sheep.cloud.entity.IRepliesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yxr
 */
public interface IRepliesEntityRepository extends JpaRepository<IRepliesEntity, Integer> {
}