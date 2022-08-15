package com.sheep.cloud.dao;

import com.sheep.cloud.entity.IResourcesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yxr
 */
public interface IResourcesEntityRepository extends JpaRepository<IResourcesEntity, Integer> {

    /**
     * 查询用户的发布记录
     *
     * @param uid 用户编号
     * @return 查询结果
     */
    List<IResourcesEntity> findAllByPublishUserUid(Integer uid);
}