package com.sheep.cloud.dao;

import com.sheep.cloud.entity.IWishReplyListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author yxr
 */
public interface IWishReplyListEntityRepository extends JpaRepository<IWishReplyListEntity, Integer>, JpaSpecificationExecutor<IWishReplyListEntity> {

}