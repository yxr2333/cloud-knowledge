package com.sheep.cloud.entity.sell;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.LinkTree;
import xyz.erupt.annotation.sub_erupt.Tree;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.ViewType;
import xyz.erupt.annotation.sub_field.sub_edit.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

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
@Entity(name = "sell_t_goods")
@Table(name = "sell_t_goods")
@Builder
@DynamicUpdate
@DynamicInsert
@Where(clause = "is_deleted = 0")
@Erupt(
        name = "商品信息",
        tree = @Tree(pid = "parent.id", expandLevel = 5),
        linkTree = @LinkTree(field = "type")
)
public class ISellGoodsEntity implements Serializable {

    @Value("${qiniu.domain}")
    private String domain;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @EruptField
    private Integer id;

    @Basic
    @Column(nullable = false)
    @EruptField(
            views = @View(
                    title = "商品名称",
                    sortable = true
            ),
            edit = @Edit(
                    title = "商品名称",
                    notNull = true,
                    search = @Search,
                    type = EditType.INPUT,
                    inputType = @InputType
            )
    )
    private String name;

    @Basic
    @EruptField(
            views = @View(
                    title = "商品描述",
                    sortable = true
            ),
            edit = @Edit(
                    title = "商品描述",
                    notNull = true,
                    type = EditType.TEXTAREA,
                    inputType = @InputType
            )
    )
    private String description;

    @Basic
    @EruptField(
            views = @View(title = "价格", type = ViewType.NUMBER),
            edit = @Edit(
                    title = "价格",
                    type = EditType.INPUT,
                    inputType = @InputType(type = "number")
            )
    )
    private Double price;

    @Basic
    @EruptField(
            views = @View(title = "品牌"),
            edit = @Edit(
                    title = "品牌",
                    type = EditType.INPUT,
                    inputType = @InputType
            )
    )
    private String brand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    @EruptField(
            views = @View(title = "商品类型", column = "name"),
            edit = @Edit(
                    title = "商品类型",
                    type = EditType.REFERENCE_TREE,
                    referenceTreeType = @ReferenceTreeType,
                    notNull = true
            )
    )
    private ISellGoodsTypeEntity type;

    @Basic
    @Column(name = "free_total")
    @EruptField(
            views = @View(title = "库存量", type = ViewType.NUMBER),
            edit = @Edit(
                    title = "库存量",
                    type = EditType.INPUT,
                    inputType = @InputType(type = "number")
            )
    )
    private Integer freeTotal = 0;

    @Basic
    @EruptField(
            views = @View(
                    title = "商品封面图片"
            ),
            edit = @Edit(
                    title = "商品封面图片",
                    notNull = true,
                    type = EditType.ATTACHMENT,
                    attachmentType = @AttachmentType(type = AttachmentType.Type.IMAGE)
            )
    )
    private String cover;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "release_user_id", referencedColumnName = "id")
    private ISellUserEntity releaseUser;

    @Basic
    @Column(name = "is_discount")
    @EruptField(
            views = @View(title = "是否打折"),
            edit = @Edit(
                    title = "是否打折",
                    type = EditType.BOOLEAN,
                    boolType = @BoolType(trueText = "是", falseText = "否")
            )
    )
    private Boolean isDiscount = false;

    @Basic
    @Column(name = "discount_percent")
    @EruptField(
            views = @View(title = "折扣率", type = ViewType.NUMBER),
            edit = @Edit(
                    title = "折扣率",
                    type = EditType.INPUT,
                    placeHolder = "折扣率：0.0~1.0",
                    inputType = @InputType(type = "number")
            )
    )
    private Double discountPercent;

    @Basic
    @Column(name = "is_down")
    @EruptField(
            views = @View(title = "是否下架"),
            edit = @Edit(
                    title = "是否下架",
                    type = EditType.BOOLEAN,
                    boolType = @BoolType(trueText = "是", falseText = "否")
            )
    )
    private Boolean isDown = false;

    @Basic
    @Column(name = "is_deleted")
    @EruptField(
            views = @View(title = "是否删除"),
            edit = @Edit(
                    title = "是否删除",
                    type = EditType.BOOLEAN,
                    boolType = @BoolType(trueText = "是", falseText = "否")
            )
    )
    private Boolean isDeleted = false;

    @Column(name = "release_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EruptField
    private LocalDateTime releaseTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EruptField
    private LocalDateTime downTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EruptField
    private LocalDateTime deleteAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ISellGoodsEntity that = (ISellGoodsEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @PrePersist
    public void prePersist() {
        this.releaseTime = LocalDateTime.now();
        if (!cover.startsWith(domain)) {
            cover = domain + cover;
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (!cover.startsWith(domain)) {
            cover = domain + cover;
        }
    }
}
