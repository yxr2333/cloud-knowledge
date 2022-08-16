package com.sheep.cloud.dao;

import com.sheep.cloud.entity.IRepliesEntity;
<<<<<<< HEAD
<<<<<<< HEAD
=======
import com.sheep.cloud.response.ApiResult;
>>>>>>> 116a605 (新增：完成评论角模块)
=======
>>>>>>> upstream/master
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
<<<<<<< HEAD
<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> 116a605 (新增：完成评论角模块)
=======
>>>>>>> upstream/master

/**
 * @author yxr
 */
public interface IRepliesEntityRepository extends JpaRepository<IRepliesEntity, Integer> {
    /*
     * @Description: 根据评论id判断是否存在回复
     * @param comment_id
<<<<<<< HEAD
<<<<<<< HEAD
     * @return: boolean
=======
     * @return: Integer
>>>>>>> 116a605 (新增：完成评论角模块)
=======
     * @return: boolean
>>>>>>> upstream/master
     */
    @Query(nativeQuery = true,value = "select count(*) from t_replies where comment_id=?1")
    Integer existsCommentById(Integer comment_id);

<<<<<<< HEAD
<<<<<<< HEAD
=======
    /*
     * @Description: 根据评论id删除回复
     * @param comment_id
     * @return: void
     */
>>>>>>> 116a605 (新增：完成评论角模块)
=======
>>>>>>> upstream/master
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "delete from t_replies where comment_id=?1")
    void deleteCommentById(Integer comment_id);
<<<<<<< HEAD
<<<<<<< HEAD
=======

    /*
     * @Description:根据评论id查询回复
     * @param comment_id
     * @return: java.util.List<com.sheep.cloud.entity.IRepliesEntity>
     */
    @Query(nativeQuery = true,value = "select * from t_replies where comment_id=?1")
    List<IRepliesEntity> getReplyByCommentId(Integer comment_id);
>>>>>>> 116a605 (新增：完成评论角模块)
=======
>>>>>>> upstream/master
}