package com.sheep.cloud.dto.response.sell;

import lombok.*;

import java.io.Serializable;

/**
 * A DTO for the {@link com.sheep.cloud.model.IUserEntity} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IUserEntityBaseInfo implements Serializable {
    private Integer id;
    private String username;
    private String description;
    private String email;
    private String avatar;
}