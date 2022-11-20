package com.sheep.cloud.dto.request.sell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.dto.request
 * @datetime 2022/10/9 星期日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyRefundOrderParam implements Serializable {


    private static final long serialVersionUID = -4136084465427859031L;

    private String orderId;
    private String reason;
}
