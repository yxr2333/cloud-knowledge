package com.sheep.cloud.entity.sell;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Value;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.AttachmentType;

import javax.persistence.*;
import java.io.Serializable;
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
@Entity
@Table(name = "sell_t_images")
@Erupt(
        name = "商品图片"
)
public class ISellImageEntity implements Serializable {

    @Value("${qiniu.domain}")
    private String domain;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EruptField
    private Integer id;

    @Basic
    @Column(nullable = false, unique = true)
    @EruptField(
            views = @View(
                    title = "图片编号"
            ),
            edit = @Edit(
                    title = "图片编号",
                    notNull = true
            )
    )
    private String imgId;

    @Basic
    @Column(nullable = false)
    @EruptField(
            views = @View(
                    title = "图片地址"
            ),
            edit = @Edit(
                    title = "图片地址",
                    notNull = true,
                    type = EditType.ATTACHMENT,
                    attachmentType = @AttachmentType(type = AttachmentType.Type.IMAGE)
            )
    )
    private String imgUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "good_id", referencedColumnName = "id")
    @EruptField(
            views = @View(
                    title = "商品",
                    column = "name"
            ),
            edit = @Edit(
                    title = "商品",
                    type = EditType.REFERENCE_TABLE,
                    notNull = true
            )
    )
    private ISellGoodsEntity good;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ISellImageEntity that = (ISellImageEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @PreUpdate
    public void PreUpdate() {
        imgUrl = domain + imgUrl;
    }

    @PrePersist
    public void prePersist() {
        imgUrl = domain + imgUrl;
    }
}