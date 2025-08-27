package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "终端保存成员请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalSaveMemberRequest.class */
public class TerminalSaveMemberRequest implements Serializable {
    private static final long serialVersionUID = 2946433761342889663L;

    @ApiModelProperty(name = "orgId", value = "orgId")
    private Long orgId;

    @ApiModelProperty(name = "deptId", value = "部门Id,多个部门用'，'隔开：如'1,2'")
    private String deptId;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "memberJobNum", value = "工号")
    private String memberJobNum;

    @ApiModelProperty(name = "eigenvalueValue", value = "人脸特征值")
    private String eigenvalueValue;

    @ApiModelProperty(name = "verifyFaceUrl", value = "人脸照片url")
    private String verifyFaceUrl;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setEigenvalueValue(String eigenvalueValue) {
        this.eigenvalueValue = eigenvalueValue;
    }

    public void setVerifyFaceUrl(String verifyFaceUrl) {
        this.verifyFaceUrl = verifyFaceUrl;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSaveMemberRequest)) {
            return false;
        }
        TerminalSaveMemberRequest other = (TerminalSaveMemberRequest) o;
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
        Object this$memberName = getMemberName();
        Object other$memberName = other.getMemberName();
        if (this$memberName == null) {
            if (other$memberName != null) {
                return false;
            }
        } else if (!this$memberName.equals(other$memberName)) {
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
        Object this$eigenvalueValue = getEigenvalueValue();
        Object other$eigenvalueValue = other.getEigenvalueValue();
        if (this$eigenvalueValue == null) {
            if (other$eigenvalueValue != null) {
                return false;
            }
        } else if (!this$eigenvalueValue.equals(other$eigenvalueValue)) {
            return false;
        }
        Object this$verifyFaceUrl = getVerifyFaceUrl();
        Object other$verifyFaceUrl = other.getVerifyFaceUrl();
        return this$verifyFaceUrl == null ? other$verifyFaceUrl == null : this$verifyFaceUrl.equals(other$verifyFaceUrl);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSaveMemberRequest;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $deptId = getDeptId();
        int result2 = (result * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $memberName = getMemberName();
        int result3 = (result2 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result4 = (result3 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $eigenvalueValue = getEigenvalueValue();
        int result5 = (result4 * 59) + ($eigenvalueValue == null ? 43 : $eigenvalueValue.hashCode());
        Object $verifyFaceUrl = getVerifyFaceUrl();
        return (result5 * 59) + ($verifyFaceUrl == null ? 43 : $verifyFaceUrl.hashCode());
    }

    public String toString() {
        return "TerminalSaveMemberRequest(orgId=" + getOrgId() + ", deptId=" + getDeptId() + ", memberName=" + getMemberName() + ", memberJobNum=" + getMemberJobNum() + ", eigenvalueValue=" + getEigenvalueValue() + ", verifyFaceUrl=" + getVerifyFaceUrl() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public String getEigenvalueValue() {
        return this.eigenvalueValue;
    }

    public String getVerifyFaceUrl() {
        return this.verifyFaceUrl;
    }
}
