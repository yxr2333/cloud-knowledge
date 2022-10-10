package com.sheep.cloud.model;

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

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_wish_buy")
@Accessors(chain = true)
public class IWishBuyEntity {

    /*
      id int [pk, note: "求购编号"]
      gtype int [ref: > t_types.id]
      description varchar [note: "求购描述"]
      pub_time datetime [note: "发布时间"]
      pub_user int [ref: > t_users.uid]
      is_finished boolean [note: "求购是否完成"]
      helper int [ref: > t_users.uid]
      img_url varchar [note: "图片链接地址"]
      finish_time datetime [note: "求购结束时间"]
      g_name varchar [note: "满足条件的商品名称"]
      g_cover varchar [note: "满足条件的商品的封面图"]
      good int [ref: < t_goods.id]
      is_down boolean [note: "是否被发布者撤回"]
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private IGoodsTypeEntity type;

    @Basic
    private String description;

    @Basic
    private String imgUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime pubTime = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "pub_user_id", referencedColumnName = "id")
    private IUserEntity pubUser;

    @Basic
    private Boolean isFinished = false;

    @ManyToOne
    @JoinColumn(name = "helper_id", referencedColumnName = "id")
    private IUserEntity helper;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishTime;

    @Basic
    private String goodName;

    @Basic
    private String goodCover;

    @ManyToOne
    @JoinColumn(name = "good_id", referencedColumnName = "id")
    private IGoodsEntity good;

    @Basic
    private Boolean isDown = false;


}