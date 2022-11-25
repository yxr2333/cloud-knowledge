package com.sheep.cloud.entity.sell;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.cloud.sheep.model
 * @datetime 2022/9/16 星期五
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sell_t_spikes")
public class ISellSpikesEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    private Integer id;

    @Basic
    private String sid;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startDate;
}
