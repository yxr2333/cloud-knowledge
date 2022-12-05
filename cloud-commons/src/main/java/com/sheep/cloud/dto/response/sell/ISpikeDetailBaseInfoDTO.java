package com.sheep.cloud.dto.response.sell;

import lombok.Data;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/12/5 星期一
 * Happy Every Coding Time~
 */
@Data
public class ISpikeDetailBaseInfoDTO {
    private Integer id;
    private IGoodsSimpleInfoDTO goods;
    private Double price;
    private Integer spikeId;
    private String spikeName;
}
