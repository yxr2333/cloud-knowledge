package com.sheep.cloud.entity.knowledge;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.ViewType;
import xyz.erupt.annotation.sub_field.sub_edit.AttachmentType;
import xyz.erupt.annotation.sub_field.sub_edit.InputType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.entity
 * @datetime 2022/8/11 星期四
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "t_resources")
//@Erupt(name = "资源", power = @Power(importable = true, export = true))
public class IResourcesEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    @EruptField(
            views = @View(title = "资源名称", sortable = true),
            edit = @Edit(title = "资源名称", notNull = true, search = @Search, type = EditType.INPUT)
    )
    private String name;

    @Basic
    @Column(name = "description")
    @EruptField(
            views = @View(
                    title = "资源描述"
            ),
            edit = @Edit(
                    title = "资源描述",
                    notNull = true,
                    type = EditType.TEXTAREA,
                    inputType = @InputType
            )
    )
    private String description;

    @Basic
    @Column(name = "link")
    @EruptField(
            views = @View(
                    title = "资源链接",
                    type = ViewType.LINK
            ),
            edit = @Edit(
                    title = "资源链接",
                    notNull = true,
                    type = EditType.INPUT,
                    inputType = @InputType
            )
    )
    private String link;
    @Basic
    @Column(name = "icon")
    @EruptField(
            views = @View(
                    title = "资源图片"
            ),
            edit = @Edit(
                    title = "资源图片",
                    type = EditType.ATTACHMENT, notNull = true,
                    attachmentType = @AttachmentType(type = AttachmentType.Type.IMAGE)
            )
    )
    private String icon;

    @Basic
    @Column(name = "content")
    private String content;
    @Basic
    @Column(name = "collect")
    private Integer collect;
    @Basic
    @Column(name = "is_paid")
    private Boolean isPaid;
    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "release_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releaseTime;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "uid")
    private IUsersEntity publishUser;


    @ManyToMany
    @ToString.Exclude
    @JoinTable(name = "t_resources_labels", joinColumns = @JoinColumn(name = "resource_id"), inverseJoinColumns = @JoinColumn(name = "label_id"))
    private List<ILabelsEntity> labels;
}
