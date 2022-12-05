package com.sheep.cloud.dto.response.sell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Zhang Jinming
 * @date 5/12/2022 下午9:52
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IOrderUserInfoDTO {

    private Integer id;

    private String oid;

    private String goodName;

    private String goodTypeName;

    private String goodCover;

    private Double price;

    private Integer orderStatus;

    private String orderStatusDescription;

    private String address;

    private LocalDateTime createTime;

    private LocalDateTime payTime;

    private LocalDateTime finishTime;
}
