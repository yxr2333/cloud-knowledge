package com.sheep.cloud.dao;

import com.sheep.cloud.entity.IUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author yxr
 */
public interface IUsersEntityRepository extends JpaRepository<IUsersEntity, Integer> {

    /**
     * 根据用户名判断用户是否存在
     *
     * @param username 用户名
     * @return 用户是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    Optional<IUsersEntity> findIUsersEntityByUsername(String username);

    /**
     * 通过用户名模糊查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    List<IUsersEntity> findAllByUsernameLike(String username);
}