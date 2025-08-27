package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "生成openApi的app_key")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/OpenApiAppKeyRequest.class */
public class OpenApiAppKeyRequest implements Serializable {
    private static final long serialVersionUID = -8764992279420203374L;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OpenApiAppKeyRequest)) {
            return false;
        }
        OpenApiAppKeyRequest other = (OpenApiAppKeyRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        return this$orgId == null ? other$orgId == null : this$orgId.equals(other$orgId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof OpenApiAppKeyRequest;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        return result;
    }

    public String toString() {
        return "OpenApiAppKeyRequest(orgId=" + getOrgId() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }
}
