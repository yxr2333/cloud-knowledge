package com.sheep.cloud.service.impl;

import com.sheep.cloud.dao.sell.ISellAddressEntityRepository;
import com.sheep.cloud.dao.sell.ISellUserEntityRepository;
import com.sheep.cloud.dto.request.sell.AddressParam;
import com.sheep.cloud.dto.request.sell.SaveAddressParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.sell.IAddressInfoDTO;
import com.sheep.cloud.entity.sell.ISellAddressEntity;
import com.sheep.cloud.entity.sell.ISellUserEntity;
import com.sheep.cloud.service.IAddressControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/12/5 星期一
 * Happy Every Coding Time~
 */
@Service
public class IAddressControlServiceImpl implements IAddressControlService {

    @Autowired
    private ISellAddressEntityRepository addressRepository;
    @Autowired
    private ISellUserEntityRepository userRepository;

    /**
     * 查询某个用户的收货地址
     *
     * @param userId 用户id
     * @return 收货地址列表
     */
    @Override
    public ApiResult<?> findAddressByUserId(Integer userId) {
        List<ISellAddressEntity> list = addressRepository.findAllByUserId(userId);
        List<IAddressInfoDTO> address = list.stream()
                .map(item -> {
                    IAddressInfoDTO addressInfoDTO = new IAddressInfoDTO();
                    addressInfoDTO.setAddress(item.getAddress());
                    addressInfoDTO.setId(item.getId());
                    addressInfoDTO.setPhone(item.getPhone());
                    addressInfoDTO.setName(item.getName());
                    return addressInfoDTO;
                })
                .collect(Collectors.toList());
        return new ApiResult<>().success(address);
    }

    /**
     * 更新地址信息
     *
     * @param addressParam 新的地址信息
     * @return 更新结果
     */
    @Override
    public ApiResult<?> updateAddress(AddressParam addressParam) {
        Integer addressId = addressParam.getId();
        ISellAddressEntity sellAddress = addressRepository.findById(addressId).orElseThrow(() -> new RuntimeException("暂时不能处理"));
        sellAddress.setAddress(addressParam.getAddress());
        sellAddress.setName(addressParam.getName());
        sellAddress.setPhone(addressParam.getPhone());
        addressRepository.save(sellAddress);
        return new ApiResult<>().success("更新成功");
    }

    /**
     * 新建地址信息
     *
     * @param saveAddressParam 地址信息
     * @return 新建结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> saveAddress(SaveAddressParam saveAddressParam) {
        Integer userId = saveAddressParam.getUserId();
        ISellUserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("暂时不能处理"));
        ISellAddressEntity address = ISellAddressEntity.builder()
                .user(user)
                .address(saveAddressParam.getAddress())
                .phone(saveAddressParam.getPhone())
                .name(saveAddressParam.getName())
                .build();
        addressRepository.save(address);
        return new ApiResult<>().success("新建成功");
    }

    /**
     * 查询地址信息
     *
     * @param id 地址id
     * @return 地址信息
     */
    @Override
    public ApiResult<?> findAddressById(Integer id) {
        ISellAddressEntity addressEntity = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("暂未找到相关信息"));
        addressEntity.setUser(null);
        return new ApiResult<>().success(addressEntity);
    }

    @Override
    public ApiResult<?> deleteAddress(Integer addressId) {
        addressRepository.deleteById(addressId);
        return new ApiResult<>().success("删除成功");
    }
}
