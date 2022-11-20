package com.sheep.cloud.entity.knowledge;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

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
