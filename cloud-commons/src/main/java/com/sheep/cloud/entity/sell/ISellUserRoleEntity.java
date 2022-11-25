package com.sheep.cloud.entity.sell;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.cloud.sheep.model
 * @datetime 2022/9/16 星期五
 */

import lombok.*;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.Power;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.View;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "sell_t_user_roles")
@Erupt(
        name = "用户角色",
        power = @Power(importable = true, export = true)
)
public class ISellUserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @EruptField
    private Integer id;

    @Basic
    @Column(name = "role_name")
    @EruptField(
            views = @View(
                    title = "角色名"
            ),
            edit = @Edit(
                    title = "角色名",
                    notNull = true
            )
    )
    private String name;

    @Basic
    @EruptField(
            views = @View(
                    title = "角色描述"
            ),
            edit = @Edit(
                    title = "角色描述",
                    notNull = true
            )
    )
    private String label;
}