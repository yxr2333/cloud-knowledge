package com.sheep.cloud.dao;

import com.sheep.cloud.entity.ICommentsEntity;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> upstream/master
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
<<<<<<< HEAD
=======
import com.sheep.cloud.response.ICommentGetInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
>>>>>>> 116a605 (新增：完成评论角模块)
=======
>>>>>>> upstream/master

/**
 * @author yxr
 */
public interface ICommentsEntityRepository extends JpaRepository<ICommentsEntity, Integer> {
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> upstream/master
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update t_comments set collect=collect+1 where id=?1")
    Integer collectById(Integer id);
<<<<<<< HEAD
=======
    /*
     * @Description: 根据资源id查询评论
     * @param resource_id
     * @return: java.util.List<com.sheep.cloud.entity.ICommentsEntity>
     */
    @Query(nativeQuery = true,value = "select * from t_comments where resource_id=?1")
    List<ICommentsEntity> findCommentByResourceId(Integer resource);
>>>>>>> 116a605 (新增：完成评论角模块)
=======
>>>>>>> upstream/master
}