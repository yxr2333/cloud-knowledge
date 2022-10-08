package com.sheep.cloud.model;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.cloud.sheep.model
 * @datetime 2022/9/16 星期五
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
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
@Table(name = "t_orders")
@Builder
public class IOrdersEntity implements Serializable {

    /*
      id int [pk]
      oid varchar [note: "订单流水号"]
      good int [ref: > t_goods.id]
      price double [note: "订单金额"]
      is_discount boolean [note: "是否打折"]
      discount_percent double [note: "折扣率"]
      sell_type int [note: "交易模式（0：普通交易，1：秒杀交易）"]
      final_price double [note: '折后价格']
      sellerId int [ref: > t_users.uid]
      sellerName varchar [note: "出售人的名字"]
      buyer int [ref: > t_users.uid]
      buyer_name varchar [note: "购买人的名字"]
      order_status int [note: "订单状态"]
      create_time datetime [note: "订单创建时间"]
      pay_time datetime [note: "订单支付时间"]
      finish_time datetime [note: "订单完成时间"]
    */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Basic
    @Column(nullable = false, unique = true)
    private String oid;

    @ManyToOne
    @JoinColumn(name = "good_id", referencedColumnName = "id")
    private IGoodsEntity good;

    @Basic
    @Column(nullable = false)
    private Double price;

    @Basic
    @Column(nullable = false)
    private Boolean isDiscount;

    @Basic
    private Double discountPercent;

    // 交易模式（0：普通交易，1：秒杀交易）
    @Basic
    @Column(nullable = false)
    private Integer sellType;

    @Basic
    @Column(nullable = false)
    private Double finalPrice;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private IUserEntity seller;

    @Basic
    @Column(nullable = false)
    private String sellerName;

    @Basic
    @Column(nullable = false)
    private String sellerMail;

    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private IUserEntity buyer;

    @Basic
    @Column(nullable = false)
    private String buyerName;

    @Basic
    @Column(nullable = false)
    private String buyerMail;

    @Basic
    @Column(nullable = false)
    private Integer orderStatus;

    @Basic
    private String orderStatusDescription;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishTime;
}