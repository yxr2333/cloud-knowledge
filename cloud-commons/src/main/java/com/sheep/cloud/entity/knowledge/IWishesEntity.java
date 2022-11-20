package com.sheep.cloud.entity.knowledge;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "t_wishes", schema = "summer_training")
@ToString
public class IWishesEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "uid")
    private IUsersEntity user;

    @Basic
    @Column(name = "publish_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    private Boolean isFinished;

    @ManyToMany
    @JoinTable(name = "t_wishes_labels", joinColumns = @JoinColumn(name = "wish_id"), inverseJoinColumns = @JoinColumn(name = "label_id"))
    private List<ILabelsEntity> labels;
}
