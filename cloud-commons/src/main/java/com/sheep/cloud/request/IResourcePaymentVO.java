package com.sheep.cloud.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class IResourcePaymentVO implements Serializable {

    @NotNull(message = "资源编号不能为空")
    private Integer id;
    private Boolean isPaid;
    private String password;
}
