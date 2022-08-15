package com.sheep.cloud.dao;

import com.sheep.cloud.entity.IWishesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yxr
 */
public interface IWishesEntityRepository extends JpaRepository<IWishesEntity, Integer> {

    /**
     * 查询用户的心愿墙发表记录
     *
     * @param uid 用户编号
     * @return 查询结果
     */
    List<IWishesEntity> findAllByUserUid(Integer uid);
}