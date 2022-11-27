package com.sheep.cloud.entity.sell;

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
@Entity
@Table(name = "sell_t_users")
@Builder
public class ISellUserInfoEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    private Integer id;

    @Basic
    @Column(unique = true, nullable = false)
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
