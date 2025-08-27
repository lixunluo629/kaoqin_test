package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "修改机构信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/UpdateOrgInfoRequest.class */
public class UpdateOrgInfoRequest extends BaseRequest {

    @ApiModelProperty(name = "orgName", value = "机构名称")
    private String orgName;

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateOrgInfoRequest)) {
            return false;
        }
        UpdateOrgInfoRequest other = (UpdateOrgInfoRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$orgName = getOrgName();
        Object other$orgName = other.getOrgName();
        return this$orgName == null ? other$orgName == null : this$orgName.equals(other$orgName);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof UpdateOrgInfoRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $orgName = getOrgName();
        int result = (1 * 59) + ($orgName == null ? 43 : $orgName.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "UpdateOrgInfoRequest(orgName=" + getOrgName() + ")";
    }

    public String getOrgName() {
        return this.orgName;
    }
}
