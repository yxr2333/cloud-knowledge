package com.sheep.cloud.repository;

import com.sheep.cloud.model.IRefundOrderHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRefundOrderHistoryEntityRepository extends JpaRepository<IRefundOrderHistoryEntity, Integer> {
}