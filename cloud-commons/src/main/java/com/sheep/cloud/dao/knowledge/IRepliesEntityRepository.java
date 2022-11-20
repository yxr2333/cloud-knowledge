package com.sheep.cloud.dao.knowledge;

import com.sheep.cloud.entity.knowledge.IRepliesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author yxr
 */
public interface IRepliesEntityRepository extends JpaRepository<IRepliesEntity, Integer> {
    /*
     * @Description: 根据评论id判断是否存在回复
     * @param comment_id
     * @return: Integer
     */
    @Query(nativeQuery = true, value = "select count(*) from t_replies where comment_id=?1")
    Integer existsCommentById(Integer comment_id);

    /*
     * @Description: 根据评论id删除回复
     * @param comment_id
     * @return: void
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from t_replies where comment_id=?1")
    void deleteCommentById(Integer comment_id);

    /*
     * @Description:根据评论id查询回复
     * @param comment_id
     * @return: java.util.List<com.sheep.cloud.entity.knowledge.IRepliesEntity>
     */
    @Query(nativeQuery = true, value = "select * from t_replies where comment_id=?1")
    List<IRepliesEntity> getReplyByCommentId(Integer comment_id);
}