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
@Table(name = "sell_t_spike_details")
public class ISellSpikeDetailsEntity implements Serializable {

    /*
      id int [pk, note: "秒杀详情编号"]
      good_id int [ref: > t_goods.id, note: "商品编号"]
      spike_price double [note: "秒杀价格"]
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "good_id", referencedColumnName = "id")
    private ISellGoodsEntity goods;

    @Basic
    private Double spikePrice;

    @ManyToOne
    @JoinColumn(name = "spike_id", referencedColumnName = "id")
    private ISellSpikesEntity spike;
}
