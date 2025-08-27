package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "保存部门信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/SaveDeptResponse.class */
public class SaveDeptResponse implements Serializable {

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "superDeptId", value = "上级部门id")
    private Long superDeptId;

    @ApiModelProperty(name = "superDeptName", value = "上级部门名称")
    private String superDeptName;

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setSuperDeptId(Long superDeptId) {
        this.superDeptId = superDeptId;
    }

    public void setSuperDeptName(String superDeptName) {
        this.superDeptName = superDeptName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveDeptResponse)) {
            return false;
        }
        SaveDeptResponse other = (SaveDeptResponse) o;
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
        Object this$superDeptId = getSuperDeptId();
        Object other$superDeptId = other.getSuperDeptId();
        if (this$superDeptId == null) {
            if (other$superDeptId != null) {
                return false;
            }
        } else if (!this$superDeptId.equals(other$superDeptId)) {
            return false;
        }
        Object this$superDeptName = getSuperDeptName();
        Object other$superDeptName = other.getSuperDeptName();
        return this$superDeptName == null ? other$superDeptName == null : this$superDeptName.equals(other$superDeptName);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SaveDeptResponse;
    }

    public int hashCode() {
        Object $deptId = getDeptId();
        int result = (1 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deptName = getDeptName();
        int result2 = (result * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $superDeptId = getSuperDeptId();
        int result3 = (result2 * 59) + ($superDeptId == null ? 43 : $superDeptId.hashCode());
        Object $superDeptName = getSuperDeptName();
        return (result3 * 59) + ($superDeptName == null ? 43 : $superDeptName.hashCode());
    }

    public String toString() {
        return "SaveDeptResponse(deptId=" + getDeptId() + ", deptName=" + getDeptName() + ", superDeptId=" + getSuperDeptId() + ", superDeptName=" + getSuperDeptName() + ")";
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public Long getSuperDeptId() {
        return this.superDeptId;
    }

    public String getSuperDeptName() {
        return this.superDeptName;
    }
}
