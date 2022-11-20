package com.sheep.cloud.entity.knowledge;

import lombok.*;

import javax.persistence.*;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.entity
 * @datetime 2022/9/14 星期三
 */
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_app_clients", schema = "summer_training")
public class IAppClientsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int clientId;

    private String clientSecret;

    private String allowUrl;

    private String contractScope;

    private Boolean isAutoMode;
}
