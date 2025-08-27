package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "权限组修改返回信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/UpdateGroupMemberResponse.class */
public class UpdateGroupMemberResponse implements Serializable {
    private static final long serialVersionUID = 7975170523066462517L;

    @ApiModelProperty(name = "groupId", value = "权限组id")
    private Long groupId;

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateGroupMemberResponse)) {
            return false;
        }
        UpdateGroupMemberResponse other = (UpdateGroupMemberResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$groupId = getGroupId();
        Object other$groupId = other.getGroupId();
        return this$groupId == null ? other$groupId == null : this$groupId.equals(other$groupId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof UpdateGroupMemberResponse;
    }

    public int hashCode() {
        Object $groupId = getGroupId();
        int result = (1 * 59) + ($groupId == null ? 43 : $groupId.hashCode());
        return result;
    }

    public String toString() {
        return "UpdateGroupMemberResponse(groupId=" + getGroupId() + ")";
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public UpdateGroupMemberResponse(Long groupId) {
        this.groupId = groupId;
    }
}
