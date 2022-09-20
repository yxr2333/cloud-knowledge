package com.sheep.cloud.dao;

import com.sheep.cloud.entity.IAppClientsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IAppClientsEntityRepository extends JpaRepository<IAppClientsEntity, Integer>, JpaSpecificationExecutor<IAppClientsEntity> {
}