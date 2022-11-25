package com.sheep.cloud.entity.sell;

import lombok.*;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.LinkTree;
import xyz.erupt.annotation.sub_erupt.Tree;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.ReferenceTreeType;

import javax.persistence.*;
import java.util.List;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.cloud.sheep.model
 * @datetime 2022/9/16 星期五
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sell_t_shopping_cart")
@Erupt(
        name = "购物车",
        tree = @Tree(pid = "parent.id", expandLevel = 2),
        linkTree = @LinkTree(field = "goods"))
public class ISellShoppingCartEntity {

    /*
      id int [pk, note: "购物车编号"]
      uid int [ref: - t_users.uid, note: "所属用户"]
      goods int[] [ref: <> t_goods.id, note: "商品列表"]
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EruptField
    private Integer id;

    @OneToOne
    @JoinColumn(name = "uid", referencedColumnName = "id")
    private ISellUserEntity user;

    @ManyToMany
    @JoinTable(
            name = "sell_t_shopping_carts_goods",
            joinColumns = @JoinColumn(
                    name = "shopping_cart_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "goods_id",
                    referencedColumnName = "id")
    )
    @EruptField(
            views = @View(title = "商品列表", column = "name"),
            edit = @Edit(
                    title = "商品列表",
                    type = EditType.REFERENCE_TREE,
                    notNull = true,
                    referenceTreeType = @ReferenceTreeType
            )
    )
    private List<ISellGoodsEntity> goods;
}
