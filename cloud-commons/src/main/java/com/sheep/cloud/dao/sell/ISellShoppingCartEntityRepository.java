package com.sheep.cloud.dao.sell;

import com.sheep.cloud.entity.sell.ISellGoodsEntity;
import com.sheep.cloud.entity.sell.ISellShoppingCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author sheep
 */
public interface ISellShoppingCartEntityRepository extends JpaRepository<ISellShoppingCartEntity, Integer> {

    boolean existsByGoodsId(Integer goodsId);

    void deleteAllByGoodsId(Integer goodsId);

    // 对购物车的商品进行分页查询
    @Query(value = "SELECT sell_t_goods.*\n" +
            "FROM sell_t_goods\n" +
            "         LEFT JOIN (SELECT tcg.*\n" +
            "                    FROM sell_t_shopping_carts_goods tcg\n" +
            "                             LEFT JOIN (SELECT cg.goods_id\n" +
            "                                        FROM sell_t_shopping_carts_goods cg\n" +
            "                                        WHERE cg.shopping_cart_id =\n" +
            "                                              (SELECT cart.id FROM sell_t_shopping_cart cart WHERE uid = ?1)) AS t1\n" +
            "                                       ON t1.goods_id = tcg.shopping_cart_id) t2 ON sell_t_goods.id = t2.goods_id\n" +
            "LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<ISellGoodsEntity> test(Integer id, Integer limit, Integer offset);


}