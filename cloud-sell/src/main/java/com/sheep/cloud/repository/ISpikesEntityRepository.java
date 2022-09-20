package com.sheep.cloud.repository;

import com.sheep.cloud.model.ISpikesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISpikesEntityRepository extends JpaRepository<ISpikesEntity, Integer> {
}