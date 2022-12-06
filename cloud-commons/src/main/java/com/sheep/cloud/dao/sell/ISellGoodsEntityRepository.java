package com.sheep.cloud.dao.sell;

import com.sheep.cloud.entity.sell.ISellGoodsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author sheep
 */
public interface ISellGoodsEntityRepository extends JpaRepository<ISellGoodsEntity, Integer>, JpaSpecificationExecutor<ISellGoodsEntity> {

    Page<ISellGoodsEntity> findAllByReleaseUserId(Pageable pageable, Integer uid);

    @Query(nativeQuery = true, value = "select * from sell_t_goods where is_deleted = 0 and is_down = 0 order by rand() limit ?1")
    List<ISellGoodsEntity> randomFindGoods(Integer size);

    List<ISellGoodsEntity> findAllByReleaseUserId(Integer id);

    @Query("SELECT s FROM sell_t_goods s " +
            "WHERE s.name " +
            "LIKE %?1% OR s.description LIKE %?1% OR s.releaseUser.id IN " +
            "(SELECT u.id FROM s.releaseUser u " +
            "WHERE u.username " +
            "LIKE %?1% OR u.description LIKE %?1%)")
    Page<ISellGoodsEntity> findAllByKeyWord(String keyword, Pageable pageable);

}