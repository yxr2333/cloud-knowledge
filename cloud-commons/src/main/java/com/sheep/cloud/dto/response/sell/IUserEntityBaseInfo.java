package com.sheep.cloud.dto.response.sell;

import lombok.*;

import java.io.Serializable;

/**
 * @author sheep
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
    private String phone;
}