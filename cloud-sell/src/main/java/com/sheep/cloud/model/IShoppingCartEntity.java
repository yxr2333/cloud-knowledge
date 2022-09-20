package com.sheep.cloud.model;

import lombok.*;

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
@Table(name = "t_shopping_cart")
public class IShoppingCartEntity {

    /*
      id int [pk, note: "购物车编号"]
      uid int [ref: - t_users.uid, note: "所属用户"]
      goods int[] [ref: <> t_goods.id, note: "商品列表"]
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "uid", referencedColumnName = "id")
    private IUserEntity user;

    @ManyToMany
    @JoinTable(
            name = "t_shopping_carts_goods",
            joinColumns = @JoinColumn(
                    name = "shopping_cart_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "goods_id",
                    referencedColumnName = "id")
    )
    private List<IGoodsEntity> goods;
}
