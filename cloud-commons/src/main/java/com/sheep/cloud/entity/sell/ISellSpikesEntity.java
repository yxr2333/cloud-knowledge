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
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.InputType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sell_t_spikes")
@Erupt(
        name = "秒杀活动"
)
public class ISellSpikesEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @EruptField
    private Integer id;

    @Basic
    @EruptField(
            views = @View(
                    title = "活动编号"
            ),
            edit = @Edit(
                    title = "活动编号",
                    notNull = true,
                    desc = "活动编号",
                    search = @Search,
                    type = EditType.INPUT,
                    inputType = @InputType
            )
    )
    private String sid;

    @Basic
    @EruptField(
            views = @View(
                    title = "活动名称"
            ),
            edit = @Edit(
                    title = "活动名称",
                    notNull = true,
                    desc = "活动名称",
                    search = @Search,
                    type = EditType.INPUT,
                    inputType = @InputType
            )
    )
    private String name;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @EruptField(
            views = @View(
                    title = "起始日期"
            ),
            edit = @Edit(
                    title = "起始日期",
                    notNull = true,
                    desc = "起始日期",
                    search = @Search,
                    type = EditType.DATE
            )
    )
    private LocalDate startDate;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @EruptField(
            views = @View(
                    title = "结束日期"
            ),
            edit = @Edit(
                    title = "结束日期",
                    notNull = true,
                    desc = "结束日期",
                    search = @Search,
                    type = EditType.DATE
            )
    )
    private LocalDate endDate;
}
