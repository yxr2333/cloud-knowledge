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
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.LinkTree;
import xyz.erupt.annotation.sub_erupt.Tree;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.AttachmentType;
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
@Table(name = "sell_t_wish_buy")
@Accessors(chain = true)
@Erupt(
        name = "求购信息",
        tree = @Tree(
                pid = "parent.id",
                expandLevel = 5
        ),
        linkTree = @LinkTree(field = "type")
)
public class ISellWishBuyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @EruptField
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    @EruptField(
            views = @View(title = "求购商品类型", column = "name"),
            edit = @Edit(
                    title = "求购商品类型",
                    type = EditType.REFERENCE_TREE,
                    referenceTreeType = @ReferenceTreeType,
                    notNull = true
            )
    )
    private ISellGoodsTypeEntity type;

    @Basic
    @EruptField(
            views = @View(
                    title = "求购描述"
            ),
            edit = @Edit(
                    title = "求购描述",
                    type = EditType.TEXTAREA,
                    inputType = @InputType(type = "textarea")
            )
    )
    private String description;

    @Basic
    @EruptField(
            views = @View(
                    title = "求购商品图片"
            ),
            edit = @Edit(
                    title = "求购商品图片",
                    notNull = true,
                    type = EditType.ATTACHMENT,
                    attachmentType = @AttachmentType(type = AttachmentType.Type.IMAGE)
            )
    )
    private String imgUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EruptField
    private LocalDateTime pubTime = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "pub_user_id", referencedColumnName = "id")
    @EruptField(
            views = @View(title = "发布人", column = "username"),
            edit = @Edit(
                    title = "发布人",
                    type = EditType.REFERENCE_TREE,
                    referenceTreeType = @ReferenceTreeType(label = "username"),
                    notNull = true
            )
    )
    private ISellUserEntity pubUser;

    @Basic
    @EruptField(
            views = @View(
                    title = "是否结束"
            ),
            edit = @Edit(
                    title = "是否结束",
                    type = EditType.BOOLEAN,
                    boolType = @BoolType(trueText = "是", falseText = "否")
            )
    )
    private Boolean isFinished = false;

    @ManyToOne
    @JoinColumn(name = "helper_id", referencedColumnName = "id")
    @EruptField(
            views = @View(
                    title = "提供人",
                    column = "username"
            ),
            edit = @Edit(
                    title = "提供人",
                    type = EditType.REFERENCE_TREE,
                    referenceTreeType = @ReferenceTreeType(label = "username"),
                    notNull = true)
    )
    private ISellUserEntity helper;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EruptField
    private LocalDateTime finishTime;

    @Basic
    @EruptField(
            views = @View(
                    title = "商品名称"
            ),
            edit = @Edit(
                    title = "商品名称",
                    type = EditType.INPUT,
                    notNull = true,
                    inputType = @InputType
            )
    )
    private String goodName;

    @Basic
    @EruptField(
            views = @View(
                    title = "商品封面图"
            ),
            edit = @Edit(
                    title = "商品封面图",
                    type = EditType.ATTACHMENT,
                    attachmentType = @AttachmentType(type = AttachmentType.Type.IMAGE),
                    notNull = true
            )
    )
    private String goodCover;

    @ManyToOne
    @JoinColumn(name = "good_id", referencedColumnName = "id")
    @EruptField(
            views = @View(
                    title = "商品",
                    column = "name"
            ),
            edit = @Edit(
                    title = "商品",
                    type = EditType.REFERENCE_TREE,
                    referenceTreeType = @ReferenceTreeType,
                    notNull = true
            )
    )
    private ISellGoodsEntity good;

    @Basic
    @EruptField(
            views = @View(
                    title = "是否下架"
            ),
            edit = @Edit(
                    title = "是否下架",
                    type = EditType.BOOLEAN,
                    boolType = @BoolType(trueText = "是", falseText = "否")
            )
    )
    private Boolean isDown = false;


}