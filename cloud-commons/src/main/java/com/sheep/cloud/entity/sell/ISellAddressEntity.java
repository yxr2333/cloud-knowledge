package com.sheep.cloud.entity.sell;

import lombok.*;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.LinkTree;
import xyz.erupt.annotation.sub_erupt.Power;
import xyz.erupt.annotation.sub_erupt.Tree;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.InputType;
import xyz.erupt.annotation.sub_field.sub_edit.ReferenceTreeType;

import javax.persistence.*;

/**
 * @author WTY2002
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sell_t_address")
@Builder
@Erupt(
        name = "收货地址",
        power = @Power(importable = true, export = true),
        linkTree = @LinkTree(field = "user"),
        tree = @Tree(pid = "parent.id", expandLevel = 5, label = "address")
)
public class ISellAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EruptField
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @EruptField(
            views = @View(title = "所属用户", column = "username"),
            edit = @Edit(
                    title = "所属用户",
                    notNull = true,
                    type = EditType.REFERENCE_TREE,
                    referenceTreeType = @ReferenceTreeType(label = "username"),
                    desc = "选择用户"
            )
    )
    private ISellUserEntity user;

    @Basic
    @Column(nullable = false)
    @EruptField(
            views = @View(title = "收货地址"),
            edit = @Edit(
                    title = "收货地址",
                    notNull = true,
                    type = EditType.INPUT,
                    inputType = @InputType,
                    desc = "收货地址"
            )
    )
    private String address;
}
