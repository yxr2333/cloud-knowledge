package com.sheep.cloud.entity.knowledge;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.entity
 * @datetime 2022/8/15 星期一
 */

@Table(name = "t_label_category", schema = "summer_training")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ILabelCategoryEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @Column(length = 30)
    private String iconName;


    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<ILabelsEntity> labels;
}
