package com.sheep.cloud.dao.sell;

import com.sheep.cloud.entity.sell.ISellAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/12/5 星期一
 * Happy Every Coding Time~
 */

@Repository
public interface ISellAddressEntityRepository extends JpaRepository<ISellAddressEntity, Integer> {

    List<ISellAddressEntity> findAllByUserId(Integer userId);
}

