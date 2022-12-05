package com.sheep.cloud.dao.sell;

import com.sheep.cloud.entity.sell.ISellSpikesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author sheep
 */
public interface ISellSpikesEntityRepository extends JpaRepository<ISellSpikesEntity, Integer> {

    /**
     * 获取最近的秒杀活动
     *
     * @return 最近的秒杀活动
     */
    @Query(value = "select * from sell_t_spikes order by start_date limit ?1", nativeQuery = true)
    List<ISellSpikesEntity> findRecentSpike(Integer size);

}