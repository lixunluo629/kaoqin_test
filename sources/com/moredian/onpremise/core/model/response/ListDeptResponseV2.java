package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "部门列表信息v2")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListDeptResponseV2.class */
public class ListDeptResponseV2 implements Serializable {
    private static final long serialVersionUID = -2877096229364059382L;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "superDeptId", value = "父部门id")
    private Long superDeptId;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "childDepts", value = "子部门列表")
    private List<ListDeptResponseV2> childDepts;

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setSuperDeptId(Long superDeptId) {
        this.superDeptId = superDeptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setChildDepts(List<ListDeptResponseV2> childDepts) {
        this.childDepts = childDepts;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListDeptResponseV2)) {
            return false;
        }
        ListDeptResponseV2 other = (ListDeptResponseV2) o;
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
        return this$childDepts == null ? other$childDepts == null : this$childDepts.equals(other$childDepts);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListDeptResponseV2;
    }

    public int hashCode() {
        Object $deptId = getDeptId();
        int result = (1 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $superDeptId = getSuperDeptId();
        int result2 = (result * 59) + ($superDeptId == null ? 43 : $superDeptId.hashCode());
        Object $deptName = getDeptName();
        int result3 = (result2 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $childDepts = getChildDepts();
        return (result3 * 59) + ($childDepts == null ? 43 : $childDepts.hashCode());
    }

    public String toString() {
        return "ListDeptResponseV2(deptId=" + getDeptId() + ", superDeptId=" + getSuperDeptId() + ", deptName=" + getDeptName() + ", childDepts=" + getChildDepts() + ")";
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

    public List<ListDeptResponseV2> getChildDepts() {
        return this.childDepts;
    }
}
