package com.sheep.cloud.entity.knowledge;

import lombok.*;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.Power;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.*;

import javax.persistence.*;
import java.util.List;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.entity
 * @datetime 2022/8/11 星期四
 */
@Entity
@Table(name = "t_users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Erupt(
        name = "平台用户",
        power = @Power(importable = true, export = true),
        primaryKeyCol = "uid"
)
public class IUsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private Integer uid;

    @Basic
    @Column(name = "username")
    @EruptField(
            views = @View(
                    title = "用户名",
                    sortable = true
            ),
            edit = @Edit(
                    title = "用户名",
                    notNull = true,
                    search = @Search,
                    type = EditType.INPUT,
                    inputType = @InputType
            )
    )
    private String username;

    @Basic
    @Column(name = "password")
    @EruptField(
            views = @View(
                    title = "密码", show = false),
            edit = @Edit(
                    title = "密码",
                    notNull = true,
                    show = false,
                    type = EditType.INPUT,
                    inputType = @InputType(type = "password")
            )
    )
    private String password;

    @Basic
    @Column(name = "salt")
    @EruptField(
            views = @View(
                    title = "盐值", show = false),
            edit = @Edit(
                    title = "盐值",
                    notNull = true,
                    show = false,
                    type = EditType.INPUT,
                    inputType = @InputType(type = "password")
            )
    )
    private String salt;

    @Basic
    @Column(name = "gender")
    @EruptField(
            views = @View(title = "性别"),
            edit = @Edit(
                    title = "性别",
                    type = EditType.CHOICE,
                    choiceType = @ChoiceType(
                            vl = {
                                    @VL(label = "男", value = "男"),
                                    @VL(label = "女", value = "女")
                            }
                    )
            )
    )
    private String gender;

    @Basic
    @Column(name = "email")
    @EruptField(
            views = @View(title = "邮箱"),
            edit = @Edit(
                    title = "邮箱",
                    type = EditType.INPUT,
                    inputType = @InputType(type = "email")
            )
    )
    private String email;

    @Basic
    @Column(name = "avatar")
    @EruptField(
            views = @View(
                    title = "头像"
            ),
            edit = @Edit(
                    title = "头像",
                    type = EditType.ATTACHMENT, notNull = true,
                    attachmentType = @AttachmentType(type = AttachmentType.Type.IMAGE)
            )
    )
    private String avatar;

    @Basic
    @Column(name = "score")
    @EruptField(
            views = @View(title = "积分"),
            edit = @Edit(
                    title = "积分",
                    type = EditType.INPUT, notNull = true,
                    inputType = @InputType(type = "number")

            )
    )
    private int score;

    @Basic
    @Column(name = "description")
    @EruptField(
            views = @View(
                    title = "个人介绍"
            ),
            edit = @Edit(
                    title = "个人介绍",
                    notNull = true,
                    type = EditType.TEXTAREA,
                    inputType = @InputType
            )
    )
    private String description;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_labels_users", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "label_id"))
    private List<ILabelsEntity> labels;
}
