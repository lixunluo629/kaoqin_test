package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "设备端人脸录入响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/SaveMemberFaceResponse.class */
public class SaveMemberFaceResponse implements Serializable {
    private static final long serialVersionUID = -50184489058250510L;

    @ApiModelProperty(name = "deptName", value = "部门名称，多个之前逗号隔开，同dept_id")
    private String deptName;

    @ApiModelProperty(name = "verifyFaceUrl", value = "成员底库照")
    private String verifyFaceUrl;

    @ApiModelProperty(name = "memberName", value = "成员姓名")
    private String memberName;

    @ApiModelProperty(name = "memberMobile", value = "成员手机号")
    private String memberMobile;

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setVerifyFaceUrl(String verifyFaceUrl) {
        this.verifyFaceUrl = verifyFaceUrl;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveMemberFaceResponse)) {
            return false;
        }
        SaveMemberFaceResponse other = (SaveMemberFaceResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$verifyFaceUrl = getVerifyFaceUrl();
        Object other$verifyFaceUrl = other.getVerifyFaceUrl();
        if (this$verifyFaceUrl == null) {
            if (other$verifyFaceUrl != null) {
                return false;
            }
        } else if (!this$verifyFaceUrl.equals(other$verifyFaceUrl)) {
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
        Object this$memberMobile = getMemberMobile();
        Object other$memberMobile = other.getMemberMobile();
        return this$memberMobile == null ? other$memberMobile == null : this$memberMobile.equals(other$memberMobile);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SaveMemberFaceResponse;
    }

    public int hashCode() {
        Object $deptName = getDeptName();
        int result = (1 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $verifyFaceUrl = getVerifyFaceUrl();
        int result2 = (result * 59) + ($verifyFaceUrl == null ? 43 : $verifyFaceUrl.hashCode());
        Object $memberName = getMemberName();
        int result3 = (result2 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberMobile = getMemberMobile();
        return (result3 * 59) + ($memberMobile == null ? 43 : $memberMobile.hashCode());
    }

    public String toString() {
        return "SaveMemberFaceResponse(deptName=" + getDeptName() + ", verifyFaceUrl=" + getVerifyFaceUrl() + ", memberName=" + getMemberName() + ", memberMobile=" + getMemberMobile() + ")";
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getVerifyFaceUrl() {
        return this.verifyFaceUrl;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getMemberMobile() {
        return this.memberMobile;
    }
}
