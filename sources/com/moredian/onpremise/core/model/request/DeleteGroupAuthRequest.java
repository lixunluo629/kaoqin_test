package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "删除权限组")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeleteGroupAuthRequest.class */
public class DeleteGroupAuthRequest extends BaseRequest {

    @ApiModelProperty(name = "groupId", value = "权限组id")
    private Long groupId;

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeleteGroupAuthRequest)) {
            return false;
        }
        DeleteGroupAuthRequest other = (DeleteGroupAuthRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$groupId = getGroupId();
        Object other$groupId = other.getGroupId();
        return this$groupId == null ? other$groupId == null : this$groupId.equals(other$groupId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DeleteGroupAuthRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $groupId = getGroupId();
        int result = (1 * 59) + ($groupId == null ? 43 : $groupId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DeleteGroupAuthRequest(groupId=" + getGroupId() + ")";
    }

    public Long getGroupId() {
        return this.groupId;
    }
}
