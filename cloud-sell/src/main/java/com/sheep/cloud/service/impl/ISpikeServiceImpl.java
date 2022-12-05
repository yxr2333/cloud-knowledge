package com.sheep.cloud.service.impl;

import cn.hutool.core.util.IdUtil;
import com.sheep.cloud.dao.sell.ISellSpikeDetailsEntityRepository;
import com.sheep.cloud.dao.sell.ISellSpikesEntityRepository;
import com.sheep.cloud.dto.request.sell.CreateSpikeParam;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.sell.IGoodsSimpleInfoDTO;
import com.sheep.cloud.dto.response.sell.ISpikeDetailBaseInfoDTO;
import com.sheep.cloud.entity.sell.ISellSpikeDetailsEntity;
import com.sheep.cloud.entity.sell.ISellSpikesEntity;
import com.sheep.cloud.service.ISpikeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
public class ISpikeServiceImpl implements ISpikeService {

    @Autowired
    private ISellSpikesEntityRepository sellSpikesEntityRepository;
    @Autowired
    private ISellSpikeDetailsEntityRepository spikeDetailsEntityRepository;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * 创建一个秒杀活动
     *
     * @param vo 秒杀活动信息
     * @return 创建结果
     */
    @Override
    public ApiResult<?> createOne(CreateSpikeParam vo) {
        LocalDate startDate = vo.getStartDate();
        if (startDate.isBefore(LocalDate.now())) {
            return new ApiResult<>().fail("开始时间不能早于今天");
        }
        LocalDate endDate = vo.getEndDate();
        if (endDate.isBefore(startDate)) {
            return new ApiResult<>().fail("结束时间不能早于开始时间");
        }
        ISellSpikesEntity entity = ISellSpikesEntity.builder()
                .sid(IdUtil.randomUUID())
                .startDate(startDate)
                .endDate(endDate)
                .name(vo.getName())
                .build();
        sellSpikesEntityRepository.save(entity);
        return new ApiResult<>().success("创建成功");
    }

    /**
     * 展示最近的size条秒杀活动
     *
     * @param size 展示数量
     * @return 秒杀活动列表
     */
    @Override
    public ApiResult<?> showRecentSpike(Integer size) {
        List<ISellSpikesEntity> spikes = sellSpikesEntityRepository.findRecentSpike(size);
        return new ApiResult<>().success(spikes);
    }

    /**
     * 查找某个秒杀活动的详情
     *
     * @param spikeId 秒杀活动id
     * @return 秒杀活动详情
     */
    @Override
    public ApiResult<?> findSpikeDetails(Integer spikeId) {
        List<ISellSpikeDetailsEntity> details = spikeDetailsEntityRepository.findAllBySpikeId(spikeId);
        List<ISpikeDetailBaseInfoDTO> list = details.stream()
                .map(detail -> {
                    ISpikeDetailBaseInfoDTO baseInfoDTO = new ISpikeDetailBaseInfoDTO();
                    baseInfoDTO.setId(detail.getId());
                    baseInfoDTO.setSpikeId(detail.getSpike().getId());
                    baseInfoDTO.setSpikeName(detail.getSpike().getName());
                    IGoodsSimpleInfoDTO infoDTO = modelMapper.map(detail.getGoods(), IGoodsSimpleInfoDTO.class);
                    baseInfoDTO.setGoods(infoDTO);
                    baseInfoDTO.setPrice(detail.getSpikePrice());
                    return baseInfoDTO;
                })
                .collect(Collectors.toList());
        return new ApiResult<>().success(list);
    }
}
