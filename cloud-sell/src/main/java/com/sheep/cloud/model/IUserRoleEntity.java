package com.sheep.cloud.model;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.cloud.sheep.model
 * @datetime 2022/9/16 星期五
 */

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "t_user_roles")
public class IUserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    private Integer id;

    @Basic
    @Column(name = "role_name")
    private String name;
}