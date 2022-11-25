package com.sheep.cloud.entity.sell;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.cloud.sheep.model
 * @datetime 2022/9/16 星期五
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sell_t_goods")
@Builder
@DynamicUpdate
@DynamicInsert
@Where(clause = "is_deleted = 0")
public class ISellGoodsEntity implements Serializable {
    /*
      id int [pk, note: "商品编号"]
      name varchar [not null, note: "商品名称"]
      description varchar [note: "商品描述"]
      price double [note: "商品价格"]
      brand varchar [note: "商品品牌"]
      gtype int [ref: > t_types.id]
      free_total int [note: "商品库存量"]
      cover varchar [note: "商品的封面图片"]
      release_time datetime [note: "发布时间"]
      release_user int [ref: > t_users.uid]
      is_discount boolean [note: "是否打折"]
      discount_percent double [note: "折扣率"]
      is_solded boolean [note: "是否已经被购买"]
      is_down boolean [note: "是否下架"]
      buyer int [ref:> t_users.uid]
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    private Integer id;

    @Basic
    @Column(nullable = false)
    private String name;

    @Basic
    private String description;

    @Basic
    private Double price;

    @Basic
    private String brand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private ISellGoodsTypeEntity type;

    @Basic
    @Column(name = "free_total")
    private Integer freeTotal = 0;

    @Basic
    private String cover;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "release_user_id", referencedColumnName = "id")
    private ISellUserEntity releaseUser;

    @Basic
    @Column(name = "is_discount")
    private Boolean isDiscount = false;

    @Basic
    @Column(name = "discount_percent")
    private Double discountPercent;

    @Basic
    @Column(name = "is_down")
    private Boolean isDown = false;

    @Basic
    private Boolean isDeleted = false;

    @Column(name = "release_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releaseTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime downTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deleteAt;
}