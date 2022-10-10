package com.sheep.cloud.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link com.sheep.cloud.model.IGoodsEntity} entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IGoodsEntityCustomInfoDTO implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private String brand;
    private String cover;
}