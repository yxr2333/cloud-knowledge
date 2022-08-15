package com.sheep.cloud.dao;

import com.sheep.cloud.entity.ILabelsEntity;
import com.sheep.cloud.entity.IResourcesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author yxr
 */
public interface IResourcesEntityRepository extends JpaRepository<IResourcesEntity, Integer>, JpaSpecificationExecutor<IResourcesEntity> {

    /**
     * 查询用户的发布记录
     *
     * @param uid 用户编号
     * @return 查询结果
     */
    List<IResourcesEntity> findAllByPublishUserUid(Integer uid);

    /**
     * 根据标签划分资源
     *
     * @param labels     标签
     * @param pageable 分页条件
     * @return 查询结果
     */
    Page<IResourcesEntity> findDistinctAllByLabelsIn(List<ILabelsEntity> labels, Pageable pageable);}