package com.sheep.cloud.dto.request;

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
public class CheckRefundOrderParam implements Serializable {

    private static final long serialVersionUID = -4878796677035792243L;

    private String orderId;

    private Integer refundHistoryId;

    private Boolean isAgree;

    private String refusedReason;

}