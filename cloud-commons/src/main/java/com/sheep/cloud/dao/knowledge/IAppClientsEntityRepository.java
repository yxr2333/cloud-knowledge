package com.sheep.cloud.dao.knowledge;

import com.sheep.cloud.entity.knowledge.IAppClientsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IAppClientsEntityRepository extends JpaRepository<IAppClientsEntity, Integer>, JpaSpecificationExecutor<IAppClientsEntity> {
}