package com.sheep.cloud.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

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
@Table(name = "t_replies", schema = "summer_training")
public class IRepliesEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "publishTime")
    private Timestamp publishTime;

    @ManyToOne
    @JoinColumn(name = "last_reply_id", referencedColumnName = "id")
    private IRepliesEntity lastReplyId;

    @ManyToOne
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    private ICommentsEntity commentId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "uid")
    private IUsersEntity replyUser;
}
