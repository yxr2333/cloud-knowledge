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
import org.springframework.format.annotation.DateTimeFormat;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.Power;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.ViewType;
import xyz.erupt.annotation.sub_field.sub_edit.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sell_t_orders")
@Builder
@Erupt(
        name = "订单信息",
        power = @Power(importable = true, export = true)
)
public class ISellOrdersEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EruptField
    private Integer id;

    @Basic
    @Column(nullable = false, unique = true)
    @EruptField(
            views = @View(title = "订单流水号"),
            edit = @Edit(
                    title = "订单流水号",
                    notNull = true,
                    search = @Search,
                    type = EditType.INPUT,
                    inputType = @InputType
            )
    )
    private String oid;

    @ManyToOne
    @JoinColumn(name = "good_id", referencedColumnName = "id")
    @EruptField(
            views = @View(title = "商品名称", column = "name"),
            edit = @Edit(
                    title = "商品",
                    notNull = true,
                    type = EditType.REFERENCE_TABLE,
                    referenceTableType = @ReferenceTableType
            )
    )
    private ISellGoodsEntity good;

    @Basic
    @Column(nullable = false)
    @EruptField(
            views = @View(title = "订单金额（元）", type = ViewType.NUMBER),
            edit = @Edit(
                    title = "订单金额（元）",
                    notNull = true,
                    type = EditType.NUMBER,
                    inputType = @InputType(type = "number")
            )
    )
    private Double price;

    @Basic
    @Column(nullable = false)
    @EruptField(
            views = @View(title = "是否打折"),
            edit = @Edit(
                    title = "是否打折",
                    notNull = true,
                    type = EditType.BOOLEAN,
                    boolType = @BoolType(trueText = "是", falseText = "否")
            )
    )
    private Boolean isDiscount;

    @Basic
    @EruptField(
            views = @View(title = "折扣率（0.0~1.0）", type = ViewType.NUMBER),
            edit = @Edit(
                    title = "折扣率（0.0~1.0）",
                    type = EditType.INPUT,
                    placeHolder = "折扣率：0.0~1.0",
                    inputType = @InputType(type = "number")
            )
    )
    private Double discountPercent;

    // 交易模式（0：普通交易，1：秒杀交易）
    @Basic
    @Column(nullable = false)
    @EruptField(
            views = @View(title = "交易模式"),
            edit = @Edit(
                    title = "交易模式",
                    notNull = true,
                    type = EditType.CHOICE,
                    choiceType = @ChoiceType(
                            vl = {
                                    @VL(label = "普通交易", value = "0"),
                                    @VL(label = "秒杀交易", value = "1")
                            }
                    )
            )
    )
    private Integer sellType;

    @Basic
    @Column(nullable = false)
    @EruptField(
            views = @View(title = "折后金额（元）", type = ViewType.NUMBER),
            edit = @Edit(
                    title = "折后金额（元）",
                    notNull = true,
                    type = EditType.NUMBER,
                    inputType = @InputType(type = "number")
            )
    )
    private Double finalPrice;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    @EruptField(
            views = @View(title = "出售人", column = "username"),
            edit = @Edit(
                    title = "出售人",
                    notNull = true,
                    type = EditType.REFERENCE_TABLE,
                    referenceTableType = @ReferenceTableType(label = "username")
            )
    )
    private ISellUserEntity seller;

    @Basic
    @Column(nullable = false)
    @EruptField
    private String sellerName;

    @Basic
    @Column(nullable = false)
    @EruptField
    private String sellerMail;

    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    @EruptField(
            views = @View(title = "购买人", column = "username"),
            edit = @Edit(
                    title = "购买人",
                    notNull = true,
                    type = EditType.REFERENCE_TABLE,
                    referenceTableType = @ReferenceTableType(label = "username")
            )
    )
    private ISellUserEntity buyer;

    @Basic
    @Column(nullable = false)
    @EruptField
    private String buyerName;

    @Basic
    @Column(nullable = false)
    @EruptField
    private String buyerMail;

    @Basic
    @Column(nullable = false)
    @EruptField(
            views = @View(title = "订单状态"),
            edit = @Edit(
                    title = "订单状态",
                    notNull = true,
                    type = EditType.CHOICE,
                    choiceType = @ChoiceType(
                            vl = {
                                    @VL(label = "未支付", value = "0"),
                                    @VL(label = "已支付，未审核", value = "1"),
                                    @VL(label = "已审核，未发货", value = "2"),
                                    @VL(label = "已发货，未收货", value = "3"),
                                    @VL(label = "已收货，完成", value = "4"),
                                    @VL(label = "已取消", value = "5"),
                                    @VL(label = "已退款", value = "6"),
                                    @VL(label = "已申请退款，等待卖家处理", value = "7"),
                                    @VL(label = "卖家拒绝退款", value = "8"),
                                    @VL(label = "商品下架，订单取消", value = "9"),
                            }
                    )
            )
    )
    private Integer orderStatus;

    @Basic
    @EruptField
    private String orderStatusDescription;

    @Basic
    @Column(nullable = false)
    @EruptField(
            views = @View(title = "收货地址"),
            edit = @Edit(
                    title = "收货地址",
                    notNull = true,
                    type = EditType.INPUT,
                    inputType = @InputType
            )
    )
    private String address;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EruptField
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EruptField
    private LocalDateTime payTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EruptField
    private LocalDateTime finishTime;
}