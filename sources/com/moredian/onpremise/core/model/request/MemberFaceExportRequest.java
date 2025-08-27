package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "导出人脸请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/MemberFaceExportRequest.class */
public class MemberFaceExportRequest extends BaseRequest {
    private static final long serialVersionUID = 1760753380501079234L;

    @ApiModelProperty(name = "deptId", value = "部门id，例：1")
    private Long deptId;

    @ApiModelProperty(name = "deptIds", value = "部门id列表，例：1,2,3")
    private List<Long> deptIds;

    @ApiModelProperty(name = "memberId", value = "人员id，例：1")
    private Long memberId;

    @ApiModelProperty(name = "memberIds", value = "人员id列表，例：1,2,3")
    private List<Long> memberIds;

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setDeptIds(List<Long> deptIds) {
        this.deptIds = deptIds;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MemberFaceExportRequest)) {
            return false;
        }
        MemberFaceExportRequest other = (MemberFaceExportRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$deptId = getDeptId();
        Object other$deptId = other.getDeptId();
        if (this$deptId == null) {
            if (other$deptId != null) {
                return false;
            }
        } else if (!this$deptId.equals(other$deptId)) {
            return false;
        }
        Object this$deptIds = getDeptIds();
        Object other$deptIds = other.getDeptIds();
        if (this$deptIds == null) {
            if (other$deptIds != null) {
                return false;
            }
        } else if (!this$deptIds.equals(other$deptIds)) {
            return false;
        }
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        if (this$memberId == null) {
            if (other$memberId != null) {
                return false;
            }
        } else if (!this$memberId.equals(other$memberId)) {
            return false;
        }
        Object this$memberIds = getMemberIds();
        Object other$memberIds = other.getMemberIds();
        return this$memberIds == null ? other$memberIds == null : this$memberIds.equals(other$memberIds);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof MemberFaceExportRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $deptId = getDeptId();
        int result = (1 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deptIds = getDeptIds();
        int result2 = (result * 59) + ($deptIds == null ? 43 : $deptIds.hashCode());
        Object $memberId = getMemberId();
        int result3 = (result2 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberIds = getMemberIds();
        return (result3 * 59) + ($memberIds == null ? 43 : $memberIds.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "MemberFaceExportRequest(deptId=" + getDeptId() + ", deptIds=" + getDeptIds() + ", memberId=" + getMemberId() + ", memberIds=" + getMemberIds() + ")";
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public List<Long> getDeptIds() {
        return this.deptIds;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public List<Long> getMemberIds() {
        return this.memberIds;
    }
}
