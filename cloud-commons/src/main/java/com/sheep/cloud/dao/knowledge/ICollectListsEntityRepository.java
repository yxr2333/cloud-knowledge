package com.sheep.cloud.dao.knowledge;

import com.sheep.cloud.entity.knowledge.ICollectListsEntity;
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


    /**
     * 通过用户id和资源id取消收藏资源
     *
     * @param uid 用户id
     * @param rid 资源id
     * @return 收藏结果
     */
    void deleteByResourceIdAndUserUid(Integer rid, Integer uid);

    /**
     * 通过用户id和资源id查找收藏列表
     *
     * @param uid 用户id
     * @param rid 资源id
     * @return 查找结果
     */
    ICollectListsEntity findICollectListsEntityIdByResourceIdAndUserUid(Integer rid, Integer uid);

}