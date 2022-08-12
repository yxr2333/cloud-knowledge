package com.sheep.cloud.dao;

import com.sheep.cloud.entity.ICommentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yxr
 */
@Repository
public interface ICommentsEntityRepository extends JpaRepository<ICommentsEntity, Integer> {
}