package com.sheep.cloud.entity.sell;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.model
 * @datetime 2022/10/9 星期日
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.LinkTree;
import xyz.erupt.annotation.sub_erupt.Power;
import xyz.erupt.annotation.sub_erupt.Tree;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.BoolType;
import xyz.erupt.annotation.sub_field.sub_edit.InputType;
import xyz.erupt.annotation.sub_field.sub_edit.ReferenceTreeType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sell_t_refund_order_history")
@Builder
@Erupt(
        name = "退款订单历史记录",
        power = @Power(importable = true, export = true),
        tree = @Tree(
                pid = "parent.id",
                expandLevel = 5
        ),
        linkTree = @LinkTree(field = "order")
)
public class ISellRefundOrderHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EruptField
    private Integer id;

    // 一个订单可能有多个退款记录
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @EruptField(
            views = @View(title = "订单", column = "oid"),
            edit = @Edit(
                    title = "订单",
                    notNull = true,
                    desc = "订单",
                    type = EditType.REFERENCE_TREE,
                    referenceTreeType = @ReferenceTreeType(label = "oid")
            )
    )
    private ISellOrdersEntity order;

    // 是否退款成功：0-退款失败，1-退款成功
    @Basic
    @EruptField(
            views = @View(title = "退款状态"),
            edit = @Edit(
                    title = "退款状态",
                    notNull = true,
                    type = EditType.BOOLEAN,
                    boolType = @BoolType(trueText = "退款成功", falseText = "退款失败")
            )
    )
    private Boolean isRefunded;

    @Basic
    @Column(nullable = false)
    @EruptField(
            views = @View(title = "退款原因"),
            edit = @Edit(
                    title = "退款原因",
                    type = EditType.TEXTAREA,
                    inputType = @InputType(type = "textarea")
            )
    )
    private String refundReason;

    @Basic
    @EruptField(
            views = @View(title = "拒绝退款原因"),
            edit = @Edit(
                    title = "拒绝退款原因",
                    type = EditType.TEXTAREA,
                    inputType = @InputType(type = "textarea")
            )
    )
    private String refusedRefundReason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EruptField(
            views = @View(title = "创建时间"),
            edit = @Edit(
                    title = "创建时间",
                    type = EditType.DATE,
                    show = false
            )
    )
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EruptField(
            views = @View(title = "退款时间"),
            edit = @Edit(
                    title = "退款时间",
                    type = EditType.DATE,
                    show = false
            )
    )
    private LocalDateTime finalRefundTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EruptField(
            views = @View(title = "拒绝退款时间"),
            edit = @Edit(
                    title = "拒绝退款时间",
                    type = EditType.DATE,
                    show = false
            )
    )
    private LocalDateTime refusedRefundTime;

}
