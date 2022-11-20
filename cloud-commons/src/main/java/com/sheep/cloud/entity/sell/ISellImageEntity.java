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
@Table(name = "sell_t_images")
public class ISellImageEntity implements Serializable {
    /*
          id int [pk, note: "主键编号"]
          p_id varchar [note: "图片唯一编号"]
          p_url varchar [note: "图片链接地址"]
          goods int [ref: > t_goods.id]
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Basic
    @Column(nullable = false, unique = true)
    private String imgId;

    @Basic
    @Column(nullable = false)
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "good_id", referencedColumnName = "id")
    private ISellGoodsEntity good;
}