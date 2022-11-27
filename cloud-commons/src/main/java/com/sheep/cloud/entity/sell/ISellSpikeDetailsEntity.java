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
import xyz.erupt.annotation.sub_erupt.LinkTree;
import xyz.erupt.annotation.sub_erupt.Tree;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.ReferenceTreeType;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sell_t_spike_details")
@Erupt(
        name = "秒杀活动详情",
        tree = @Tree(
                pid = "parent.id",
                expandLevel = 5
        ),
        linkTree = @LinkTree(field = "spike")
)
public class ISellSpikeDetailsEntity implements Serializable {

    /*
      id int [pk, note: "秒杀详情编号"]
      good_id int [ref: > t_goods.id, note: "商品编号"]
      spike_price double [note: "秒杀价格"]
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @EruptField
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "good_id", referencedColumnName = "id")
    @EruptField(
            views = @View(
                    title = "商品编号",
                    column = "name"
            ),
            edit = @Edit(
                    title = "商品编号",
                    notNull = true,
                    desc = "商品编号",
                    type = EditType.REFERENCE_TABLE
            )
    )
    private ISellGoodsEntity goods;

    @Basic
    @EruptField(
            views = @View(title = "秒杀价格"),
            edit = @Edit(
                    title = "秒杀价格",
                    notNull = true,
                    type = EditType.NUMBER
            )
    )
    private Double spikePrice;

    @ManyToOne
    @JoinColumn(name = "spike_id", referencedColumnName = "id")
    @EruptField(
            views = @View(title = "秒杀活动", column = "name"),
            edit = @Edit(
                    title = "秒杀活动",
                    notNull = true,
                    desc = "秒杀活动",
                    type = EditType.REFERENCE_TREE,
                    referenceTreeType = @ReferenceTreeType
            )
    )
    private ISellSpikesEntity spike;
}
