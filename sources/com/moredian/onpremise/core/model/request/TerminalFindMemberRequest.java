package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "设备端查询员工参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalFindMemberRequest.class */
public class TerminalFindMemberRequest implements Serializable {

    @ApiModelProperty(name = "memberJobNum", value = "成员编号：工号，学号，等内部唯一编号")
    private String memberJobNum;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalFindMemberRequest)) {
            return false;
        }
        TerminalFindMemberRequest other = (TerminalFindMemberRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        return this$orgId == null ? other$orgId == null : this$orgId.equals(other$orgId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalFindMemberRequest;
    }

    public int hashCode() {
        Object $memberJobNum = getMemberJobNum();
        int result = (1 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $orgId = getOrgId();
        return (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
    }

    public String toString() {
        return "TerminalFindMemberRequest(memberJobNum=" + getMemberJobNum() + ", orgId=" + getOrgId() + ")";
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public Long getOrgId() {
        return this.orgId;
    }
}
