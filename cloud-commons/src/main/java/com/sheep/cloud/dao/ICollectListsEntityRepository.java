package com.sheep.cloud.dao;

import com.sheep.cloud.entity.ICollectListsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yxr
 */
public interface ICollectListsEntityRepository extends JpaRepository<ICollectListsEntity, Integer> {

    /**
     * 查询用户的收藏列表
     *
     * @param userId 用户编号
     * @return 收藏列表
     */
    List<ICollectListsEntity> findAllByUserUid(Integer userId);
}