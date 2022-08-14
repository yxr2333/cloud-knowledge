package com.sheep.cloud.dao;

import com.sheep.cloud.entity.ICommentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author yxr
 */
public interface ICommentsEntityRepository extends JpaRepository<ICommentsEntity, Integer> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update t_comments set collect=collect+1 where id=?1")
    Integer collectById(Integer id);
}