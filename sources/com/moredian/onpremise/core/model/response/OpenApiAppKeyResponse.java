package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "open-api对应的appKey")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/OpenApiAppKeyResponse.class */
public class OpenApiAppKeyResponse implements Serializable {

    @ApiModelProperty(name = "appKey", value = "appKey")
    private String appKey = "";

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OpenApiAppKeyResponse)) {
            return false;
        }
        OpenApiAppKeyResponse other = (OpenApiAppKeyResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$appKey = getAppKey();
        Object other$appKey = other.getAppKey();
        return this$appKey == null ? other$appKey == null : this$appKey.equals(other$appKey);
    }

    protected boolean canEqual(Object other) {
        return other instanceof OpenApiAppKeyResponse;
    }

    public int hashCode() {
        Object $appKey = getAppKey();
        int result = (1 * 59) + ($appKey == null ? 43 : $appKey.hashCode());
        return result;
    }

    public String toString() {
        return "OpenApiAppKeyResponse(appKey=" + getAppKey() + ")";
    }

    public String getAppKey() {
        return this.appKey;
    }
}
