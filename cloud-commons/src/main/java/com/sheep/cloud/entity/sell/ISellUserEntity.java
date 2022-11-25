package com.sheep.cloud.entity.sell;


import lombok.*;
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

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.cloud.sheep.model
 * @datetime 2022/9/16 星期五
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sell_t_users", uniqueConstraints = {
        @UniqueConstraint(name = "uc_iselluserentity_username", columnNames = {"username"})
})
@Builder
@Erupt(
        name = "用户",
        power = @Power(importable = true, export = true)
)
public class ISellUserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @EruptField
    private Integer id;

    @Basic
    @Column(unique = true, nullable = false)
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
    @Column(nullable = false)
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
    @Column(nullable = false)
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

    @Basic
    @EruptField(
            views = @View(title = "邮箱"),
            edit = @Edit(
                    title = "邮箱",
                    type = EditType.INPUT,
                    inputType = @InputType(type = "email")
            )
    )
    private String email;

    @EruptField(
            views = @View(
                    title = "头像"
            ),
            edit = @Edit(
                    title = "头像",
                    type = EditType.ATTACHMENT, search = @Search, notNull = true,
                    attachmentType = @AttachmentType(type = AttachmentType.Type.IMAGE)
            )
    )
    private String avatar;

    @Basic
    @EruptField(
            views = @View(title = "账户余额", type = ViewType.NUMBER),
            edit = @Edit(
                    title = "账户余额",
                    type = EditType.INPUT,
                    inputType = @InputType(type = "number")
            )
    )
    private Double freeMoney;

    @Basic
    @EruptField(
            views = @View(title = "是否被禁用", type = ViewType.BOOLEAN),
            edit = @Edit(
                    title = "是否被禁用",
                    type = EditType.BOOLEAN,
                    boolType = @BoolType(trueText = "是", falseText = "否")
            )
    )
    private Boolean isBanned;

    @EruptField
    private Boolean isBindMainAccount;

    @EruptField
    private Integer mainAccountId;

    @EruptField
    private String mainAccountAppId;

    @EruptField
    private Boolean isBindDing;

    @EruptField
    private String dingAppId;

    @EruptField(
            views = @View(title = "角色", column = "label"),
            edit = @Edit(
                    title = "角色",
                    notNull = true,
                    search = @Search,
                    type = EditType.REFERENCE_TABLE,
                    referenceTableType = @ReferenceTableType(label = "label")
            )
    )
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private ISellUserRoleEntity role;
}