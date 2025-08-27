package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "直属子部门信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListChildImmediateDeptResponse.class */
public class ListChildImmediateDeptResponse implements Serializable {

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "memberNum", value = "部门成员数")
    private Integer memberNum;

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setMemberNum(Integer memberNum) {
        this.memberNum = memberNum;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListChildImmediateDeptResponse)) {
            return false;
        }
        ListChildImmediateDeptResponse other = (ListChildImmediateDeptResponse) o;
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
        Object this$deptName = getDeptName();
        Object other$deptName = other.getDeptName();
        if (this$deptName == null) {
            if (other$deptName != null) {
                return false;
            }
        } else if (!this$deptName.equals(other$deptName)) {
            return false;
        }
        Object this$memberNum = getMemberNum();
        Object other$memberNum = other.getMemberNum();
        return this$memberNum == null ? other$memberNum == null : this$memberNum.equals(other$memberNum);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListChildImmediateDeptResponse;
    }

    public int hashCode() {
        Object $deptId = getDeptId();
        int result = (1 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deptName = getDeptName();
        int result2 = (result * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $memberNum = getMemberNum();
        return (result2 * 59) + ($memberNum == null ? 43 : $memberNum.hashCode());
    }

    public String toString() {
        return "ListChildImmediateDeptResponse(deptId=" + getDeptId() + ", deptName=" + getDeptName() + ", memberNum=" + getMemberNum() + ")";
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public Integer getMemberNum() {
        return this.memberNum;
    }
}
