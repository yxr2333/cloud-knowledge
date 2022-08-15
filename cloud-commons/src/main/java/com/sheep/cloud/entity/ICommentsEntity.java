package com.sheep.cloud.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
<<<<<<< HEAD
=======
import com.fasterxml.jackson.annotation.JsonIgnore;
>>>>>>> 116a605 (新增：完成评论角模块)
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.entity
 * @datetime 2022/8/11 星期四
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "t_comments", schema = "summer_training")
public class ICommentsEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
<<<<<<< HEAD
=======
    @JsonIgnore
>>>>>>> 116a605 (新增：完成评论角模块)
    @Column(name = "collect")
    private Integer collect;

    @JoinColumn(name = "resource_id", referencedColumnName = "id")
    @ManyToOne
    private IResourcesEntity resourceId;

    @Basic
    @Column(name = "publish_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "uid")
    private IUsersEntity publishUser;
<<<<<<< HEAD


=======
>>>>>>> 116a605 (新增：完成评论角模块)
}
