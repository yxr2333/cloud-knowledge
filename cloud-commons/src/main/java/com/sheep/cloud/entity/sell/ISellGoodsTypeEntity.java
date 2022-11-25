package com.sheep.cloud.entity.sell;

import lombok.*;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.Tree;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.InputType;
import xyz.erupt.annotation.sub_field.sub_edit.ReferenceTreeType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;

import javax.persistence.*;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.cloud.sheep.model
 * @datetime 2022/9/15 星期四
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sell_t_goods_type")
@Erupt(name = "商品类型", tree = @Tree(pid = "parent.id", expandLevel = 5))
public class ISellGoodsTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EruptField
    private Integer id;

    @Basic
    @Column(nullable = false, unique = true)
    @EruptField(
            views = @View(title = "类型名称"),
            edit = @Edit(
                    title = "类型名称",
                    notNull = true,
                    inputType = @InputType,
                    type = EditType.INPUT,
                    search = @Search
            )
    )
    private String name;

    @EruptField
    private Integer typeLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @EruptField(
            views = @View(title = "父级", column = "name"),
            edit = @Edit(
                    title = "父级",
                    type = EditType.REFERENCE_TREE,
                    referenceTreeType = @ReferenceTreeType(pid = "parent.id")
            )
    )
    private ISellGoodsTypeEntity parent;
}
