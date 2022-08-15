package com.sheep.cloud.dao;

import com.sheep.cloud.entity.ICommentsPlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yxr
 */
public interface ICommentsPlatformEntityRepository extends JpaRepository<ICommentsPlatformEntity, Integer> {
}