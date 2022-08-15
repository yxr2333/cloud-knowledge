package com.sheep.cloud.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.response
 * @datetime 2022/8/12 星期五
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageData<T> {
    private Long totalNum;
    private Integer totalPage;
    private List<T> data;
}
