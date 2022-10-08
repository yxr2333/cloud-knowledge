package com.sheep.cloud.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.dto.request
 * @datetime 2022/10/8 星期六
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayOrderParam implements Serializable {

    private static final long serialVersionUID = 2357862555432756048L;

    @NotNull(message = "未找到订单编号")
    private String orderId;

    @NotNull(message = "未找到购买人")
    private Integer buyerId;
}
