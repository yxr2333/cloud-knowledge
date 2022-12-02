package com.sheep.cloud.dto.response.sell;

import com.sheep.cloud.entity.sell.ISellUserRoleEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Zhang Jinming
 * @date 25/11/2022 下午4:27
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ISellUserInfoDTO implements Serializable {

    private Integer id;

    private String username;

    private String description;

    private String email;

    private String avatar;

    private Double freeMoney;

    private Boolean isBanned;

    private Boolean isBindMainAccount;

    private Integer mainAccountId;

    private String mainAccountAppId;

    private Boolean isBindDing;

    private String dingAppId;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private ISellUserRoleEntity role;
}
