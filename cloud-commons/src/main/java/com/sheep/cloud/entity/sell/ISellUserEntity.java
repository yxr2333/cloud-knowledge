package com.sheep.cloud.entity.sell;


/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.cloud.sheep.model
 * @datetime 2022/9/16 星期五
 */

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sell_t_users")
@Builder
public class ISellUserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    private Integer id;

    @Basic
    @Column(unique = true, nullable = false)
    private String username;

    @Basic
    @Column(nullable = false)
    private String password;

    @Basic
    @Column(nullable = false)
    private String salt;

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