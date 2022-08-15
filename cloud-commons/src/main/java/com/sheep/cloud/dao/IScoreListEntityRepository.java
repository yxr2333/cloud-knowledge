package com.sheep.cloud.dao;

import com.sheep.cloud.entity.IScoreListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yxr
 */
public interface IScoreListEntityRepository extends JpaRepository<IScoreListEntity, Integer> {

    /**
     * 查询用户的积分列表
     *
     * @param uid 用户编号
     * @return 积分列表
     */
    List<IScoreListEntity> findAllByUserUid(Integer uid);
}