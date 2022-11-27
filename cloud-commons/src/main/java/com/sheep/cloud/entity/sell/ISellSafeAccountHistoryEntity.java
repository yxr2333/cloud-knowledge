package com.sheep.cloud.entity.sell;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.Power;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.InputType;
import xyz.erupt.annotation.sub_field.sub_edit.NumberType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.model
 * @datetime 2022/10/8 星期六
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sell_t_safe_account_history")
@Builder
@Erupt(
        name = "安全账户历史记录",
        power = @Power(importable = true, export = true)
)
public class ISellSafeAccountHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EruptField
    private Integer id;

    @EruptField(
            views = @View(title = "订单编号"),
            edit = @Edit(
                    title = "订单编号",
                    notNull = true,
                    desc = "订单编号",
                    search = @Search
            )
    )
    private Integer orderId;

    // 金额
    @EruptField(
            views = @View(title = "金额"),
            edit = @Edit(
                    title = "金额",
                    notNull = true,
                    desc = "金额",
                    search = @Search(vague = true),
                    type = EditType.NUMBER,
                    numberType = @NumberType
            )
    )
    private Double price;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EruptField(
            views = @View(title = "转账时间"),
            edit = @Edit(
                    title = "转账时间",
                    notNull = true,
                    desc = "转账时间",
                    search = @Search(vague = true)
            )
    )
    private LocalDateTime transTime;

    @EruptField(
            views = @View(
                    title = "转账描述"
            ),
            edit = @Edit(
                    title = "转账描述",
                    notNull = true,
                    desc = "转账描述",
                    type = EditType.TEXTAREA,
                    inputType = @InputType(type = "textarea")
            )
    )
    private String transReason;

    @EruptField
    private Integer transType;
}
