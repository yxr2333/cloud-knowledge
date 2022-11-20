package com.sheep.cloud.dao.sell;

import com.sheep.cloud.entity.sell.ISellUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ISellUserEntityRepository extends JpaRepository<ISellUserEntity, Integer> {

    Optional<ISellUserEntity> findByDingAppId(String openId);

    Boolean existsByDingAppId(String openId);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String mail);

    Boolean existsByMainAccountAppId(String appId);

    Optional<ISellUserEntity> findByMainAccountAppId(String appId);

    Optional<ISellUserEntity> findDistinctByUsername(String username);
}