package com.sheep.cloud.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

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
@Table(name = "t_labels", schema = "summer_training")
public class ILabelsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private ILabelCategoryEntity category;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ILabelsEntity that = (ILabelsEntity) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(category, that.category);
    }

    @Override
    public String toString() {
        return "ILabelsEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
