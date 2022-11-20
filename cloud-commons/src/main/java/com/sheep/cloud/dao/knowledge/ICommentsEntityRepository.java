package com.sheep.cloud.dao.knowledge;

import com.sheep.cloud.entity.knowledge.ICommentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * @author yxr
 */
public interface ICommentsEntityRepository extends JpaRepository<ICommentsEntity, Integer> {
    /*
     * @Description: 根据资源id查询评论
     * @param resource_id
     * @return: java.util.List<com.sheep.cloud.entity.knowledge.ICommentsEntity>
     */
    @Query(nativeQuery = true, value = "select * from t_comments where resource_id=?1")
    List<ICommentsEntity> findCommentByResourceId(Integer resource);
}