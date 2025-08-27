package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "获取成员详情请求对象")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeleteMemberRequest.class */
public class DeleteMemberRequest extends BaseRequest {

    @ApiModelProperty(name = "memberIds", value = "成员id集合，多个之间逗号隔开")
    private String memberIds;

    @ApiModelProperty(name = "memberJobNums", value = "成员工号集合，多个之间逗号隔开")
    private String memberJobNums;

    public void setMemberIds(String memberIds) {
        this.memberIds = memberIds;
    }

    public void setMemberJobNums(String memberJobNums) {
        this.memberJobNums = memberJobNums;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeleteMemberRequest)) {
            return false;
        }
        DeleteMemberRequest other = (DeleteMemberRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$memberJobNums = getMemberJobNums();
        Object other$memberJobNums = other.getMemberJobNums();
        return this$memberJobNums == null ? other$memberJobNums == null : this$memberJobNums.equals(other$memberJobNums);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DeleteMemberRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $memberIds = getMemberIds();
        int result = (1 * 59) + ($memberIds == null ? 43 : $memberIds.hashCode());
        Object $memberJobNums = getMemberJobNums();
        return (result * 59) + ($memberJobNums == null ? 43 : $memberJobNums.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DeleteMemberRequest(memberIds=" + getMemberIds() + ", memberJobNums=" + getMemberJobNums() + ")";
    }

    public String getMemberIds() {
        return this.memberIds;
    }

    public String getMemberJobNums() {
        return this.memberJobNums;
    }
}
