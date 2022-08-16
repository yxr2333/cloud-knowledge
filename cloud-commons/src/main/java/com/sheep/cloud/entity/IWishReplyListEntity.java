package com.sheep.cloud.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.entity
 * @datetime 2022/8/16 星期二
 */
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_wish_reply_list")
public class IWishReplyListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String avatar;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    @ManyToOne
    @JoinColumn(name = "resource_id", referencedColumnName = "id")
    private IResourcesEntity resourcesEntity;
    
    @ManyToOne
    @JoinColumn(name = "wish_id", referencedColumnName = "id")
    private IWishesEntity wishesEntity;
}
