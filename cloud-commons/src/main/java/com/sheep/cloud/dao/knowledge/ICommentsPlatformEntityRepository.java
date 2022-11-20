package com.sheep.cloud.dao.knowledge;

import com.sheep.cloud.entity.knowledge.ICommentsPlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yxr
 */
public interface ICommentsPlatformEntityRepository extends JpaRepository<ICommentsPlatformEntity, Integer> {
}