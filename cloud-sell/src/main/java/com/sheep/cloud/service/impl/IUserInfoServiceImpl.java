package com.sheep.cloud.service.impl;

import com.sheep.cloud.dao.sell.ISellUserEntityRepository;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.entity.sell.ISellUserEntity;
import com.sheep.cloud.entity.sell.ISellUserInfoEntity;
import com.sheep.cloud.service.IUserInfoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zhang Jinming
 * @date 25/11/2022 下午2:32
 */
@Service
public class IUserInfoServiceImpl implements IUserInfoService {
    @Autowired
    private ISellUserEntityRepository userEntityRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ApiResult<?> publishOne() {
        return null;
    }

    @Override
    public ApiResult<?> updateOne() {
        return null;
    }

    @Override
    public ApiResult<?> findUserInfoDetail(Integer id) {
        ISellUserEntity userEntity = userEntityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("未查询到用户信息"));
        ISellUserInfoEntity baseInfoDTO = modelMapper.map(userEntity, ISellUserInfoEntity.class);
        return new ApiResult<>().success(baseInfoDTO);
    }
}
