package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "设备查询部门列表响应")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalDeptResponse.class */
public class TerminalDeptResponse implements Serializable {
    private static final long serialVersionUID = -4981933868239663521L;

    @ApiModelProperty(name = "orgId", value = "orgId")
    private Long orgId;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "childDeptList", value = "子部门")
    private List<TerminalDeptResponse> childDeptList;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setChildDeptList(List<TerminalDeptResponse> childDeptList) {
        this.childDeptList = childDeptList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalDeptResponse)) {
            return false;
        }
        TerminalDeptResponse other = (TerminalDeptResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        if (this$orgId == null) {
            if (other$orgId != null) {
                return false;
            }
        } else if (!this$orgId.equals(other$orgId)) {
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
        Object this$childDeptList = getChildDeptList();
        Object other$childDeptList = other.getChildDeptList();
        return this$childDeptList == null ? other$childDeptList == null : this$childDeptList.equals(other$childDeptList);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalDeptResponse;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $deptId = getDeptId();
        int result2 = (result * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deptName = getDeptName();
        int result3 = (result2 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $childDeptList = getChildDeptList();
        return (result3 * 59) + ($childDeptList == null ? 43 : $childDeptList.hashCode());
    }

    public String toString() {
        return "TerminalDeptResponse(orgId=" + getOrgId() + ", deptId=" + getDeptId() + ", deptName=" + getDeptName() + ", childDeptList=" + getChildDeptList() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public List<TerminalDeptResponse> getChildDeptList() {
        return this.childDeptList;
    }
}
