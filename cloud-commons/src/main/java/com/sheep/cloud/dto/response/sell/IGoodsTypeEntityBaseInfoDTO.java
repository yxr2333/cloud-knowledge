package com.sheep.cloud.dto.response.sell;

import lombok.*;

import java.io.Serializable;

/**
 * @author sheep
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IGoodsTypeEntityBaseInfoDTO implements Serializable {
    private Integer id;
    private String name;
    private Integer typeLevel;
}