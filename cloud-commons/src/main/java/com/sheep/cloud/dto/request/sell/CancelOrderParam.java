package com.sheep.cloud.dto.request.sell;

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
public class CancelOrderParam implements Serializable {

    private static final long serialVersionUID = 3373646551906113115L;

    @NotNull(message = "未找到订单信息")
    private String orderId;

    @NotNull(message = "未找到用户信息")
    private Integer submitUserId;
}
