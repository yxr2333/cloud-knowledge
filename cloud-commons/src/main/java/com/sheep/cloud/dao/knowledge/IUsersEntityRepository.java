package com.sheep.cloud.dao.knowledge;

import com.sheep.cloud.entity.knowledge.ILabelsEntity;
import com.sheep.cloud.entity.knowledge.IUsersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author yxr
 */
public interface IUsersEntityRepository extends JpaRepository<IUsersEntity, Integer>, JpaSpecificationExecutor<IUsersEntity> {

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
     * @param pageable 分页条件
     * @return 用户信息
     */
    Page<IUsersEntity> findAllByUsernameLike(String username, Pageable pageable);

    /**
     * 通过用户标签查询用户的信息
     *
     * @param labels   标签
     * @param pageable 分页条件
     * @return 用户信息
     */
    Page<IUsersEntity> findDistinctAllByLabelsIn(Collection<ILabelsEntity> labels, Pageable pageable);

    Page<IUsersEntity> findAllByUsernameLikeAndLabelsIn(String username, List<ILabelsEntity> labels, Pageable pageable);


    Page<IUsersEntity> findAllByLabelsIn(List<ILabelsEntity> labels, Pageable pageable);
}