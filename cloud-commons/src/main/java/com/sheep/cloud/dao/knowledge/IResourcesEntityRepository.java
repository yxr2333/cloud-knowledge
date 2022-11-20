package com.sheep.cloud.dao.knowledge;

import com.sheep.cloud.entity.knowledge.ILabelsEntity;
import com.sheep.cloud.entity.knowledge.IResourcesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collections;
import java.util.List;

/**
 * @author yxr
 */
public interface IResourcesEntityRepository extends JpaRepository<IResourcesEntity, Integer>, JpaSpecificationExecutor<IResourcesEntity> {

    /**
     * 查询用户的发布记录
     *
     * @param uid 用户编号
     * @return 查询结果
     */
    List<IResourcesEntity> findAllByPublishUserUid(Integer uid);

    /**
     * 根据标签划分资源
     *
     * @param labels   标签
     * @param pageable 分页条件
     * @return 查询结果
     */
    Page<IResourcesEntity> findDistinctAllByLabelsIn(List<ILabelsEntity> labels, Pageable pageable);
//    /**

    /**
     * 查询用户收藏记录
     *
     * @param uid 用户id
     * @return 查询结果
     */
    @Query("select a from IResourcesEntity a where a.id in (select b.resource.id from ICollectListsEntity  b where b.user.uid = ?1)")
    List<IResourcesEntity> findAllByUserId(Integer uid);

    /**
     * 查询指定标签下的资源个数
     *
     * @param id 标签id
     * @return 查询结果
     */
    int countDistinctByLabelsId(Integer id);

    /**
     * 按收藏量排序查询所有资源
     *
     * @param pageable 分页条件
     * @return 查询结果
     */
    @Query("select i from IResourcesEntity i order by i.collect desc")
    Page<IResourcesEntity> findAllOrderByCollect(Pageable pageable);

    /**
     * 按发布时间排序查询所有资源
     *
     * @param pageable 分页条件
     * @return 查询结果
     */
    @Query("select i from IResourcesEntity i order by i.releaseTime desc")
    Page<IResourcesEntity> findAllOrderByRelease_time(Pageable pageable);

    /**
     * 根据标签划分资源
     *
     * @param labels 标签
     * @return 查询结果
     */
    @Query("select distinct i from IResourcesEntity i where i.labels in ?1")
    List<IResourcesEntity> myFind(Collections labels);

//    /**
//     * 根据标签划分资源
//     *
//     * @param labels   标签
//     * @param isFree   是否付费
//     * @param pageable 分页条件
//     * @return 查询结果
//     */
//    @Query("select distinct i from IResourcesEntity i where i.isPaid = ?1 and exists (select a.id from i.labels a where a = ?2)")
//    Page<IResourcesEntity> findPay(boolean isFree, ILabelsEntity labels, Pageable pageable);

    /**
     * 根据标签划分资源
     *
     * @param labels   标签
     * @param isFree   是否付费
     * @param pageable 分页条件
     * @return 查询结果
     */
    Page<IResourcesEntity> findDistinctAllByIsPaidAndAndLabelsIn(boolean isFree, List<ILabelsEntity> labels, Pageable pageable);

    Page<IResourcesEntity> findAllByIsPaidAndLabelsIn(boolean isPaid, List<ILabelsEntity> labels, Pageable pageable);

    Page<IResourcesEntity> findAllByIsPaidAndNameLikeIgnoreCaseAndLabelsIn(boolean isPaid, String name, List<ILabelsEntity> labels, Pageable pageable);
}


