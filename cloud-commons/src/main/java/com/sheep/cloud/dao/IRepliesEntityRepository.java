package com.sheep.cloud.dao;

import com.sheep.cloud.entity.IRepliesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @author yxr
 */
public interface IRepliesEntityRepository extends JpaRepository<IRepliesEntity, Integer> {
    /*
     * @Description: 根据评论id判断是否存在回复
     * @param comment_id
     * @return: boolean
     */
    @Query(nativeQuery = true,value = "select count(*) from t_replies where comment_id=?1")
    Integer existsCommentById(Integer comment_id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "delete from t_replies where comment_id=?1")
    void deleteCommentById(Integer comment_id);
}