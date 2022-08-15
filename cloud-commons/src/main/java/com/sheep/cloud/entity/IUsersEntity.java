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
@Entity
@Table(name = "t_users", schema = "summer_training")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IUsersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "uid")
    private int uid;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "salt")
    private String salt;
    @Basic
    @Column(name = "gender")
    private String gender;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "avatar")
    private String avatar;
    @Basic
    @Column(name = "score")
    private int score;
    @Basic
    @Column(name = "description")
    private String description;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_labels_users", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "label_id"))
    private List<ILabelsEntity> labels;
}
