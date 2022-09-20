package com.sheep.cloud.dao;

import com.sheep.cloud.entity.IOAuth2GrantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface IOAuth2GrantEntityRepository extends JpaRepository<IOAuth2GrantEntity, Integer>, JpaSpecificationExecutor<IOAuth2GrantEntity> {

    @Query("select i from IOAuth2GrantEntity i where i.client.clientId = ?1 and i.user.uid = ?2")
    IOAuth2GrantEntity findByClientIdAndUserUid(int clientId, int uid);

}