package com.sheep.cloud.repository;

import com.sheep.cloud.model.IUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserEntityRepository extends JpaRepository<IUserEntity, Integer> {

    Optional<IUserEntity> findByDingAppId(String openId);

    Boolean existsByDingAppId(String openId);

    Boolean existsByUsername(String username);

    Optional<IUserEntity> findDistinctByUsername(String username);
}