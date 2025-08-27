package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApiModel(description = "部门列表信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListDeptResponse.class */
public class ListDeptResponse implements Serializable {

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "superDeptId", value = "父部门id")
    private Long superDeptId;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "childDepts", value = "子部门列表")
    private List<ListDeptResponse> childDepts;

    @ApiModelProperty(name = "deptMemberLists", value = "部门下成员列表")
    private List<DeptMemberListResponse> deptMemberLists = new ArrayList();

    @ApiModelProperty(name = "memberNum", value = "所含成员数", hidden = true)
    private Integer memberNum;

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setSuperDeptId(Long superDeptId) {
        this.superDeptId = superDeptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setChildDepts(List<ListDeptResponse> childDepts) {
        this.childDepts = childDepts;
    }

    public void setDeptMemberLists(List<DeptMemberListResponse> deptMemberLists) {
        this.deptMemberLists = deptMemberLists;
    }

    public void setMemberNum(Integer memberNum) {
        this.memberNum = memberNum;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListDeptResponse)) {
            return false;
        }
        ListDeptResponse other = (ListDeptResponse) o;
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
        Object this$superDeptId = getSuperDeptId();
        Object other$superDeptId = other.getSuperDeptId();
        if (this$superDeptId == null) {
            if (other$superDeptId != null) {
                return false;
            }
        } else if (!this$superDeptId.equals(other$superDeptId)) {
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
        Object this$childDepts = getChildDepts();
        Object other$childDepts = other.getChildDepts();
        if (this$childDepts == null) {
            if (other$childDepts != null) {
                return false;
            }
        } else if (!this$childDepts.equals(other$childDepts)) {
            return false;
        }
        Object this$deptMemberLists = getDeptMemberLists();
        Object other$deptMemberLists = other.getDeptMemberLists();
        if (this$deptMemberLists == null) {
            if (other$deptMemberLists != null) {
                return false;
            }
        } else if (!this$deptMemberLists.equals(other$deptMemberLists)) {
            return false;
        }
        Object this$memberNum = getMemberNum();
        Object other$memberNum = other.getMemberNum();
        return this$memberNum == null ? other$memberNum == null : this$memberNum.equals(other$memberNum);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListDeptResponse;
    }

    public int hashCode() {
        Object $deptId = getDeptId();
        int result = (1 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $superDeptId = getSuperDeptId();
        int result2 = (result * 59) + ($superDeptId == null ? 43 : $superDeptId.hashCode());
        Object $deptName = getDeptName();
        int result3 = (result2 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $childDepts = getChildDepts();
        int result4 = (result3 * 59) + ($childDepts == null ? 43 : $childDepts.hashCode());
        Object $deptMemberLists = getDeptMemberLists();
        int result5 = (result4 * 59) + ($deptMemberLists == null ? 43 : $deptMemberLists.hashCode());
        Object $memberNum = getMemberNum();
        return (result5 * 59) + ($memberNum == null ? 43 : $memberNum.hashCode());
    }

    public String toString() {
        return "ListDeptResponse(deptId=" + getDeptId() + ", superDeptId=" + getSuperDeptId() + ", deptName=" + getDeptName() + ", childDepts=" + getChildDepts() + ", deptMemberLists=" + getDeptMemberLists() + ", memberNum=" + getMemberNum() + ")";
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public Long getSuperDeptId() {
        return this.superDeptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public List<ListDeptResponse> getChildDepts() {
        return this.childDepts;
    }

    public List<DeptMemberListResponse> getDeptMemberLists() {
        return this.deptMemberLists;
    }

    public Integer getMemberNum() {
        return this.memberNum;
    }
}
