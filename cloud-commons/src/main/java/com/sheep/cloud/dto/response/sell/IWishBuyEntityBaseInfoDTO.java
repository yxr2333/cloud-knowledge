package com.sheep.cloud.dto.response.sell;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.sheep.cloud.model.IWishBuyEntity} entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IWishBuyEntityBaseInfoDTO implements Serializable {

    private Integer id;

    private IGoodsTypeEntityBaseInfoDTO type;

    private String description;

    private String imgUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private LocalDateTime pubTime = LocalDateTime.now();

    private IUserEntityBaseInfo pubUser;

    private Boolean isFinished;

    private IUserEntityBaseInfo helper;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishTime;

    private IGoodsEntityCustomInfoDTO good;

    private Boolean isDown = false;
}