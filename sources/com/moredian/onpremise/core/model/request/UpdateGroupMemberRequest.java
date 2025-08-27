package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.model.dto.GroupMemberDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "修改权限组成员")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/UpdateGroupMemberRequest.class */
public class UpdateGroupMemberRequest extends BaseRequest {

    @ApiModelProperty(name = "groupId", value = "权限组ID")
    private Long groupId;

    @ApiModelProperty(name = "groupName", value = "权限组名称")
    private String groupName;

    @ApiModelProperty(name = "opType", value = "操作成员组类型，1-新增，2-删除")
    private Integer opType;

    @ApiModelProperty(name = "groupMembers", value = "权限组操作成员列表")
    private List<GroupMemberDto> groupMembers;

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setOpType(Integer opType) {
        this.opType = opType;
    }

    public void setGroupMembers(List<GroupMemberDto> groupMembers) {
        this.groupMembers = groupMembers;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateGroupMemberRequest)) {
            return false;
        }
        UpdateGroupMemberRequest other = (UpdateGroupMemberRequest) o;
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
        if (this$groupName == null) {
            if (other$groupName != null) {
                return false;
            }
        } else if (!this$groupName.equals(other$groupName)) {
            return false;
        }
        Object this$opType = getOpType();
        Object other$opType = other.getOpType();
        if (this$opType == null) {
            if (other$opType != null) {
                return false;
            }
        } else if (!this$opType.equals(other$opType)) {
            return false;
        }
        Object this$groupMembers = getGroupMembers();
        Object other$groupMembers = other.getGroupMembers();
        return this$groupMembers == null ? other$groupMembers == null : this$groupMembers.equals(other$groupMembers);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof UpdateGroupMemberRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $groupId = getGroupId();
        int result = (1 * 59) + ($groupId == null ? 43 : $groupId.hashCode());
        Object $groupName = getGroupName();
        int result2 = (result * 59) + ($groupName == null ? 43 : $groupName.hashCode());
        Object $opType = getOpType();
        int result3 = (result2 * 59) + ($opType == null ? 43 : $opType.hashCode());
        Object $groupMembers = getGroupMembers();
        return (result3 * 59) + ($groupMembers == null ? 43 : $groupMembers.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "UpdateGroupMemberRequest(groupId=" + getGroupId() + ", groupName=" + getGroupName() + ", opType=" + getOpType() + ", groupMembers=" + getGroupMembers() + ")";
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Integer getOpType() {
        return this.opType;
    }

    public List<GroupMemberDto> getGroupMembers() {
        return this.groupMembers;
    }
}
