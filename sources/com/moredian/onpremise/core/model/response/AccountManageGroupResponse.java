package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "账户管理权限组响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/AccountManageGroupResponse.class */
public class AccountManageGroupResponse implements Serializable {
    private static final long serialVersionUID = -3400682636890916768L;

    @ApiModelProperty(name = "groupId", value = "权限组id")
    private Long groupId;

    @ApiModelProperty(name = "groupName", value = "权限组名称")
    private String groupName;

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AccountManageGroupResponse)) {
            return false;
        }
        AccountManageGroupResponse other = (AccountManageGroupResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$groupId = getGroupId();
        Object other$groupId = other.getGroupId();
        if (this$groupId == null) {
            if (other$groupId != null) {
                return false;
            }
        } else if (!this$groupId.equals(other$groupId)) {
            return false;
        }
        Object this$groupName = getGroupName();
        Object other$groupName = other.getGroupName();
        return this$groupName == null ? other$groupName == null : this$groupName.equals(other$groupName);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AccountManageGroupResponse;
    }

    public int hashCode() {
        Object $groupId = getGroupId();
        int result = (1 * 59) + ($groupId == null ? 43 : $groupId.hashCode());
        Object $groupName = getGroupName();
        return (result * 59) + ($groupName == null ? 43 : $groupName.hashCode());
    }

    public String toString() {
        return "AccountManageGroupResponse(groupId=" + getGroupId() + ", groupName=" + getGroupName() + ")";
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public String getGroupName() {
        return this.groupName;
    }
}
