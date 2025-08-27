package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "导出人脸请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeleteFaceRequest.class */
public class DeleteFaceRequest extends BaseRequest {
    private static final long serialVersionUID = 818691909007059583L;

    @ApiModelProperty(name = "memberId", value = "单条删除填：人员id，例：1")
    private Long memberId;

    @ApiModelProperty(name = "memberIds", value = "批量删除填：人员id列表，例：1,2,3")
    private List<Long> memberIds;

    @ApiModelProperty(name = "memberJobNum", value = "单条删除填：人员工号，例：\"1\"")
    private String memberJobNum;

    @ApiModelProperty(name = "memberJobNums", value = "批量删除填：人员工号列表，例：[\"1\",\"2\",\"3\"]")
    private List<String> memberJobNums;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setMemberJobNums(List<String> memberJobNums) {
        this.memberJobNums = memberJobNums;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeleteFaceRequest)) {
            return false;
        }
        DeleteFaceRequest other = (DeleteFaceRequest) o;
        if (!other.canEqual(this)) {
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
        if (this$memberIds == null) {
            if (other$memberIds != null) {
                return false;
            }
        } else if (!this$memberIds.equals(other$memberIds)) {
            return false;
        }
        Object this$memberJobNum = getMemberJobNum();
        Object other$memberJobNum = other.getMemberJobNum();
        if (this$memberJobNum == null) {
            if (other$memberJobNum != null) {
                return false;
            }
        } else if (!this$memberJobNum.equals(other$memberJobNum)) {
            return false;
        }
        Object this$memberJobNums = getMemberJobNums();
        Object other$memberJobNums = other.getMemberJobNums();
        return this$memberJobNums == null ? other$memberJobNums == null : this$memberJobNums.equals(other$memberJobNums);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DeleteFaceRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberIds = getMemberIds();
        int result2 = (result * 59) + ($memberIds == null ? 43 : $memberIds.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result3 = (result2 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $memberJobNums = getMemberJobNums();
        return (result3 * 59) + ($memberJobNums == null ? 43 : $memberJobNums.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DeleteFaceRequest(memberId=" + getMemberId() + ", memberIds=" + getMemberIds() + ", memberJobNum=" + getMemberJobNum() + ", memberJobNums=" + getMemberJobNums() + ")";
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public List<Long> getMemberIds() {
        return this.memberIds;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public List<String> getMemberJobNums() {
        return this.memberJobNums;
    }
}
