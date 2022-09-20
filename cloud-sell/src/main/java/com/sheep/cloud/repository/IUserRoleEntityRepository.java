package com.sheep.cloud.repository;

import com.sheep.cloud.model.IUserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRoleEntityRepository extends JpaRepository<IUserRoleEntity, Integer> {
}