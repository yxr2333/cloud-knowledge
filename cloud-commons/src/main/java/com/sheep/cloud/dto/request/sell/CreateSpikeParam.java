package com.sheep.cloud.dto.request.sell;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/12/5 星期一
 * Happy Every Coding Time~
 */
@Data
public class CreateSpikeParam {

    @NotNull(message = "秒杀活动名称不能为空")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "秒杀活动开始时间不能为空")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "秒杀活动结束时间不能为空")
    private LocalDate endDate;


}
