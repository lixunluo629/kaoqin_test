package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "批量修改成员部门")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/BatchUpdateMemberDeptRequest.class */
public class BatchUpdateMemberDeptRequest extends BaseRequest {

    @ApiModelProperty(name = "oldDeptId", value = "原部门id")
    private Long oldDeptId;

    @ApiModelProperty(name = "newDeptIds", value = "新部门id集合，逗号隔开")
    private String newDeptIds;

    @ApiModelProperty(name = "memberIds", value = "选中成员id集合")
    private String memberIds;

    public void setOldDeptId(Long oldDeptId) {
        this.oldDeptId = oldDeptId;
    }

    public void setNewDeptIds(String newDeptIds) {
        this.newDeptIds = newDeptIds;
    }

    public void setMemberIds(String memberIds) {
        this.memberIds = memberIds;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BatchUpdateMemberDeptRequest)) {
            return false;
        }
        BatchUpdateMemberDeptRequest other = (BatchUpdateMemberDeptRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$oldDeptId = getOldDeptId();
        Object other$oldDeptId = other.getOldDeptId();
        if (this$oldDeptId == null) {
            if (other$oldDeptId != null) {
                return false;
            }
        } else if (!this$oldDeptId.equals(other$oldDeptId)) {
            return false;
        }
        Object this$newDeptIds = getNewDeptIds();
        Object other$newDeptIds = other.getNewDeptIds();
        if (this$newDeptIds == null) {
            if (other$newDeptIds != null) {
                return false;
            }
        } else if (!this$newDeptIds.equals(other$newDeptIds)) {
            return false;
        }
        Object this$memberIds = getMemberIds();
        Object other$memberIds = other.getMemberIds();
        return this$memberIds == null ? other$memberIds == null : this$memberIds.equals(other$memberIds);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof BatchUpdateMemberDeptRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $oldDeptId = getOldDeptId();
        int result = (1 * 59) + ($oldDeptId == null ? 43 : $oldDeptId.hashCode());
        Object $newDeptIds = getNewDeptIds();
        int result2 = (result * 59) + ($newDeptIds == null ? 43 : $newDeptIds.hashCode());
        Object $memberIds = getMemberIds();
        return (result2 * 59) + ($memberIds == null ? 43 : $memberIds.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "BatchUpdateMemberDeptRequest(oldDeptId=" + getOldDeptId() + ", newDeptIds=" + getNewDeptIds() + ", memberIds=" + getMemberIds() + ")";
    }

    public Long getOldDeptId() {
        return this.oldDeptId;
    }

    public String getNewDeptIds() {
        return this.newDeptIds;
    }

    public String getMemberIds() {
        return this.memberIds;
    }
}
