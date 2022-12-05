package com.sheep.cloud.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.sheep.cloud.dao.sell.ISellUserEntityRepository;
import com.sheep.cloud.dto.request.sell.UpdateLoginPasswordParam;
import com.sheep.cloud.dto.request.sell.UpdateUserEmailParam;
import com.sheep.cloud.dto.request.sell.UpdateUserInfoParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.entity.sell.ISellUserEntity;
import com.sheep.cloud.dto.response.sell.ISellUserInfoDTO;
import com.sheep.cloud.service.IUserInfoService;
import com.sheep.cloud.store.RedisUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> updateEmail(UpdateUserEmailParam emailParam) {
        String requestKey = emailParam.getRequestKey();
        Object o = redisUtil.get(requestKey);
        if (o == null) {
            return new ApiResult<>().error("未获取验证码或验证码已过期");
        }
        String code = o.toString();
        if (code.equals(emailParam.getCode())) {
            ISellUserEntity userEntity = userEntityRepository.findById(emailParam.getId())
                    .orElseThrow(() -> new RuntimeException("未查询到用户信息"));
            userEntity.setEmail(emailParam.getEmail());
            userEntityRepository.save(userEntity);
            redisUtil.delete(requestKey);
            return new ApiResult<>().success("邮箱更新成功！");
        }
        return new ApiResult<>().error("验证码不正确！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> updateBasicInfo(UpdateUserInfoParam userInfo) {
        ISellUserEntity userEntity = userEntityRepository.findById(userInfo.getId())
                .orElseThrow(() -> new RuntimeException("未查询到用户信息"));
        userEntity.setUsername(userInfo.getUsername());
        userEntity.setDescription(userInfo.getDescription());
        userEntity.setAvatar(userInfo.getAvatar());
        userEntityRepository.save(userEntity);
        return new ApiResult<>().success("信息更新成功！");
    }

    @Override
    public ApiResult<?> findUserInfoDetail(Integer id) {
        ISellUserEntity userEntity = userEntityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("未查询到用户信息"));
        ISellUserInfoDTO baseInfoDTO = modelMapper.map(userEntity, ISellUserInfoDTO.class);
        return new ApiResult<>().success(baseInfoDTO);
    }

    /**
     * 更新用户的邮箱
     *
     * @param email  新的邮箱地址
     * @param userId 用户编号
     * @return 更新结果
     */
    @Override
    public ApiResult<?> updateSimpleEmail(String email, Integer userId) {
        ISellUserEntity userEntity = userEntityRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("未查询到用户信息"));
        userEntity.setEmail(email);
        userEntityRepository.save(userEntity);
        return new ApiResult<>().success("邮箱更新成功！");
    }

    /**
     * 更新用户的登录密码
     *
     * @param param 更新参数
     * @return 更新结果
     */
    @Override
    public ApiResult<?> updateLoginPassword(UpdateLoginPasswordParam param) {
        Object o = redisUtil.get(param.getRequestKey());
        if (o == null) {
            return new ApiResult<>().error("未获取验证码或验证码已过期");
        }
        String code = o.toString();
        if (code.equals(param.getCode())) {
            // 删除验证码
            ISellUserEntity userEntity = userEntityRepository.findById(param.getUserId()).orElseThrow(() -> new RuntimeException("未查询到用户信息"));
            String salt = userEntity.getSalt();
            String oldPassword = userEntity.getPassword();
            String md5 = SecureUtil.md5(param.getOldPassword() + salt);
            if (!oldPassword.equals(md5)) {
                throw new RuntimeException("旧密码不正确");
            }
            String newPassword = SecureUtil.md5(param.getNewPassword() + salt);
            userEntity.setPassword(newPassword);
            userEntityRepository.save(userEntity);
            redisUtil.delete(param.getRequestKey());
            return new ApiResult<>().success("密码更新成功！");
        } else {
            return new ApiResult<>().error("验证码不正确！");
        }
    }
}
