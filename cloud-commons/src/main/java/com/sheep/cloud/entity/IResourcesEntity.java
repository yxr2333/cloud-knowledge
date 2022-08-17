package com.sheep.cloud.entity;

import lombok.*;

import javax.persistence.*;
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
@Table(name = "t_resources", schema = "summer_training")
public class IResourcesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "link")
    private String link;
    @Basic
    @Column(name = "icon")
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

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "uid")
    private IUsersEntity publishUser;


    @ManyToMany
    @ToString.Exclude
    @JoinTable(name = "t_resources_labels", joinColumns = @JoinColumn(name = "resource_id"), inverseJoinColumns = @JoinColumn(name = "label_id"))
    private List<ILabelsEntity> labels;
}
