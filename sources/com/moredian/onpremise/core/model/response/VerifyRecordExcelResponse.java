package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "识别记录导出响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/VerifyRecordExcelResponse.class */
public class VerifyRecordExcelResponse implements Serializable {

    @ApiModelProperty(name = "memberName", value = "成员姓名")
    private String memberName;

    @ApiModelProperty(name = "jobNum", value = "成员工号")
    private String jobNum;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "verifyTime", value = "识别时间")
    private Date verifyTime;

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VerifyRecordExcelResponse)) {
            return false;
        }
        VerifyRecordExcelResponse other = (VerifyRecordExcelResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$jobNum = getJobNum();
        Object other$jobNum = other.getJobNum();
        if (this$jobNum == null) {
            if (other$jobNum != null) {
                return false;
            }
        } else if (!this$jobNum.equals(other$jobNum)) {
            return false;
        }
        Object this$deviceName = getDeviceName();
        Object other$deviceName = other.getDeviceName();
        if (this$deviceName == null) {
            if (other$deviceName != null) {
                return false;
            }
        } else if (!this$deviceName.equals(other$deviceName)) {
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
        Object this$verifyTime = getVerifyTime();
        Object other$verifyTime = other.getVerifyTime();
        return this$verifyTime == null ? other$verifyTime == null : this$verifyTime.equals(other$verifyTime);
    }

    protected boolean canEqual(Object other) {
        return other instanceof VerifyRecordExcelResponse;
    }

    public int hashCode() {
        Object $memberName = getMemberName();
        int result = (1 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $jobNum = getJobNum();
        int result2 = (result * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
        Object $deviceName = getDeviceName();
        int result3 = (result2 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $deptName = getDeptName();
        int result4 = (result3 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $verifyTime = getVerifyTime();
        return (result4 * 59) + ($verifyTime == null ? 43 : $verifyTime.hashCode());
    }

    public String toString() {
        return "VerifyRecordExcelResponse(memberName=" + getMemberName() + ", jobNum=" + getJobNum() + ", deviceName=" + getDeviceName() + ", deptName=" + getDeptName() + ", verifyTime=" + getVerifyTime() + ")";
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getJobNum() {
        return this.jobNum;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public Date getVerifyTime() {
        return this.verifyTime;
    }
}
