package com.sheep.cloud.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.entity
 * @datetime 2022/8/13 星期六
 */
@Entity
@Table(name = "t_collect_lists", schema = "summer_training")
@Data
@NoArgsConstructor
public class ICollectListsEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    private IUsersEntity user;

    @JoinColumn(name = "resource_id", referencedColumnName = "id")
    @ManyToOne
    private IResourcesEntity resource;

    @Basic
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
