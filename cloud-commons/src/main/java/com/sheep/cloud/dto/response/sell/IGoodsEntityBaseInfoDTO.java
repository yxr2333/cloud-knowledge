package com.sheep.cloud.dto.response.sell;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.sheep.cloud.model.IGoodsEntity} entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IGoodsEntityBaseInfoDTO implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private String brand;
    private IGoodsTypeEntityBaseInfoDTO type;
    private Integer freeTotal = 0;
    private String cover;
    private IUserEntityBaseInfo releaseUser;
    private Boolean isDiscount = false;
    private Double discountPercent;
    private Boolean isDown = false;
    private Boolean isDeleted = false;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releaseTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime downTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deleteAt;
}