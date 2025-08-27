package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "open-api对应的appToken")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/OpenApiAppTokenResponse.class */
public class OpenApiAppTokenResponse implements Serializable {

    @ApiModelProperty(name = "appToken", value = "appToken")
    private String appToken = "";

    @ApiModelProperty(name = "expires", value = "过期时间（秒）")
    private Integer expires = 0;

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public void setExpires(Integer expires) {
        this.expires = expires;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OpenApiAppTokenResponse)) {
            return false;
        }
        OpenApiAppTokenResponse other = (OpenApiAppTokenResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$appToken = getAppToken();
        Object other$appToken = other.getAppToken();
        if (this$appToken == null) {
            if (other$appToken != null) {
                return false;
            }
        } else if (!this$appToken.equals(other$appToken)) {
            return false;
        }
        Object this$expires = getExpires();
        Object other$expires = other.getExpires();
        return this$expires == null ? other$expires == null : this$expires.equals(other$expires);
    }

    protected boolean canEqual(Object other) {
        return other instanceof OpenApiAppTokenResponse;
    }

    public int hashCode() {
        Object $appToken = getAppToken();
        int result = (1 * 59) + ($appToken == null ? 43 : $appToken.hashCode());
        Object $expires = getExpires();
        return (result * 59) + ($expires == null ? 43 : $expires.hashCode());
    }

    public String toString() {
        return "OpenApiAppTokenResponse(appToken=" + getAppToken() + ", expires=" + getExpires() + ")";
    }

    public String getAppToken() {
        return this.appToken;
    }

    public Integer getExpires() {
        return this.expires;
    }
}
