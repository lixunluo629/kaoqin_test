package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "就餐记录列表响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListMealRecordResponse.class */
public class ListMealRecordResponse implements Serializable {

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "snapFaceUrl", value = "抓拍照url")
    private String snapFaceUrl;

    @ApiModelProperty(name = "jobNum", value = "成员工号")
    private String jobNum;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "canteenName", value = "餐厅名称")
    private String canteenName;

    @ApiModelProperty(name = "recordType", value = "记录类型：1-早餐，2-午餐，3-晚餐，4-宵夜")
    private Integer recordType;

    @ApiModelProperty(name = "verifyTime", value = "识别时间")
    private Long verifyTime;

    @ApiModelProperty(name = "verifyScore", value = "识别相似度分数，百分制")
    private Integer verifyScore;

    @ApiModelProperty(name = "verifyResult", value = "识别结果：1-成功，2-无权限，3-超限消费")
    private Integer verifyResult;

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setSnapFaceUrl(String snapFaceUrl) {
        this.snapFaceUrl = snapFaceUrl;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public void setVerifyTime(Long verifyTime) {
        this.verifyTime = verifyTime;
    }

    public void setVerifyScore(Integer verifyScore) {
        this.verifyScore = verifyScore;
    }

    public void setVerifyResult(Integer verifyResult) {
        this.verifyResult = verifyResult;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListMealRecordResponse)) {
            return false;
        }
        ListMealRecordResponse other = (ListMealRecordResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$snapFaceUrl = getSnapFaceUrl();
        Object other$snapFaceUrl = other.getSnapFaceUrl();
        if (this$snapFaceUrl == null) {
            if (other$snapFaceUrl != null) {
                return false;
            }
        } else if (!this$snapFaceUrl.equals(other$snapFaceUrl)) {
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
        Object this$memberName = getMemberName();
        Object other$memberName = other.getMemberName();
        if (this$memberName == null) {
            if (other$memberName != null) {
                return false;
            }
        } else if (!this$memberName.equals(other$memberName)) {
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
        Object this$canteenName = getCanteenName();
        Object other$canteenName = other.getCanteenName();
        if (this$canteenName == null) {
            if (other$canteenName != null) {
                return false;
            }
        } else if (!this$canteenName.equals(other$canteenName)) {
            return false;
        }
        Object this$recordType = getRecordType();
        Object other$recordType = other.getRecordType();
        if (this$recordType == null) {
            if (other$recordType != null) {
                return false;
            }
        } else if (!this$recordType.equals(other$recordType)) {
            return false;
        }
        Object this$verifyTime = getVerifyTime();
        Object other$verifyTime = other.getVerifyTime();
        if (this$verifyTime == null) {
            if (other$verifyTime != null) {
                return false;
            }
        } else if (!this$verifyTime.equals(other$verifyTime)) {
            return false;
        }
        Object this$verifyScore = getVerifyScore();
        Object other$verifyScore = other.getVerifyScore();
        if (this$verifyScore == null) {
            if (other$verifyScore != null) {
                return false;
            }
        } else if (!this$verifyScore.equals(other$verifyScore)) {
            return false;
        }
        Object this$verifyResult = getVerifyResult();
        Object other$verifyResult = other.getVerifyResult();
        return this$verifyResult == null ? other$verifyResult == null : this$verifyResult.equals(other$verifyResult);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListMealRecordResponse;
    }

    public int hashCode() {
        Object $deviceName = getDeviceName();
        int result = (1 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $snapFaceUrl = getSnapFaceUrl();
        int result2 = (result * 59) + ($snapFaceUrl == null ? 43 : $snapFaceUrl.hashCode());
        Object $jobNum = getJobNum();
        int result3 = (result2 * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
        Object $memberName = getMemberName();
        int result4 = (result3 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $deptName = getDeptName();
        int result5 = (result4 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $canteenName = getCanteenName();
        int result6 = (result5 * 59) + ($canteenName == null ? 43 : $canteenName.hashCode());
        Object $recordType = getRecordType();
        int result7 = (result6 * 59) + ($recordType == null ? 43 : $recordType.hashCode());
        Object $verifyTime = getVerifyTime();
        int result8 = (result7 * 59) + ($verifyTime == null ? 43 : $verifyTime.hashCode());
        Object $verifyScore = getVerifyScore();
        int result9 = (result8 * 59) + ($verifyScore == null ? 43 : $verifyScore.hashCode());
        Object $verifyResult = getVerifyResult();
        return (result9 * 59) + ($verifyResult == null ? 43 : $verifyResult.hashCode());
    }

    public String toString() {
        return "ListMealRecordResponse(deviceName=" + getDeviceName() + ", snapFaceUrl=" + getSnapFaceUrl() + ", jobNum=" + getJobNum() + ", memberName=" + getMemberName() + ", deptName=" + getDeptName() + ", canteenName=" + getCanteenName() + ", recordType=" + getRecordType() + ", verifyTime=" + getVerifyTime() + ", verifyScore=" + getVerifyScore() + ", verifyResult=" + getVerifyResult() + ")";
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getSnapFaceUrl() {
        return this.snapFaceUrl;
    }

    public String getJobNum() {
        return this.jobNum;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getCanteenName() {
        return this.canteenName;
    }

    public Integer getRecordType() {
        return this.recordType;
    }

    public Long getVerifyTime() {
        return this.verifyTime;
    }

    public Integer getVerifyScore() {
        return this.verifyScore;
    }

    public Integer getVerifyResult() {
        return this.verifyResult;
    }
}
