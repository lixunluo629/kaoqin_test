package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "生成openApi的appToken")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/OpenApiAppTokenRequest.class */
public class OpenApiAppTokenRequest implements Serializable {

    @ApiModelProperty(name = "appKey", value = "appKey")
    private String appKey;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_ORG_CODE_KEY, value = "机构码")
    private String orgCode;

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OpenApiAppTokenRequest)) {
            return false;
        }
        OpenApiAppTokenRequest other = (OpenApiAppTokenRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$appKey = getAppKey();
        Object other$appKey = other.getAppKey();
        if (this$appKey == null) {
            if (other$appKey != null) {
                return false;
            }
        } else if (!this$appKey.equals(other$appKey)) {
            return false;
        }
        Object this$orgCode = getOrgCode();
        Object other$orgCode = other.getOrgCode();
        return this$orgCode == null ? other$orgCode == null : this$orgCode.equals(other$orgCode);
    }

    protected boolean canEqual(Object other) {
        return other instanceof OpenApiAppTokenRequest;
    }

    public int hashCode() {
        Object $appKey = getAppKey();
        int result = (1 * 59) + ($appKey == null ? 43 : $appKey.hashCode());
        Object $orgCode = getOrgCode();
        return (result * 59) + ($orgCode == null ? 43 : $orgCode.hashCode());
    }

    public String toString() {
        return "OpenApiAppTokenRequest(appKey=" + getAppKey() + ", orgCode=" + getOrgCode() + ")";
    }

    public String getAppKey() {
        return this.appKey;
    }

    public String getOrgCode() {
        return this.orgCode;
    }
}
