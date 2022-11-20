package com.sheep.cloud.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/11/18 星期五
 * Happy Every Coding Time~
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuth2TokenInfo {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("refresh_expires_in")
    private Long refreshExpiresIn;

    @JsonProperty("client_id")
    private Long clientId;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("openid")
    private String openId;
}
