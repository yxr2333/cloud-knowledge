package com.sheep.cloud.dao;

import com.sheep.cloud.entity.ILabelsEntity;
import com.sheep.cloud.entity.IWishesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author yxr
 */
public interface IWishesEntityRepository extends JpaRepository<IWishesEntity, Integer>, JpaSpecificationExecutor<IWishesEntity> {

    /**
     * 查询用户的心愿墙发表记录
     *
     * @param uid 用户编号
     * @return 查询结果
     */
    List<IWishesEntity> findAllByUserUid(Integer uid);


    /**
     * 查询用户的心愿墙发表记录
     *
     * @param uid      用户编号
     * @param pageable 分页条件
     * @return 查询结果
     */
    Page<IWishesEntity> findAllByUserUid(Integer uid, Pageable pageable);

    /**
     * 通过内容和标签搜索
     *
     * @param content  内容
     * @param labels   标签列表
     * @param pageable 分页条件
     * @return 查询结果
     */
    Page<IWishesEntity> findAllByContentLikeIgnoreCaseAndLabelsIn(String content, List<ILabelsEntity> labels, Pageable pageable);

    Page<IWishesEntity> findAllByContentLikeIgnoreCase(String content, Pageable pageable);

    Page<IWishesEntity> findAllByLabelsIn(List<ILabelsEntity> labels, Pageable pageable);
}