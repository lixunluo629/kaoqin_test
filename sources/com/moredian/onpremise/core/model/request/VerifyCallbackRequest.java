package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "回调通知参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/VerifyCallbackRequest.class */
public class VerifyCallbackRequest implements Serializable {
    private static final long serialVersionUID = 7618798527207721733L;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "snapFaceBase64", value = "抓拍照baseB4码")
    private String snapFaceBase64;

    @ApiModelProperty(name = "snapFaceUrl", value = "抓拍照url")
    private String snapFaceUrl;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "jobNum", value = "成员工号")
    private String jobNum;

    @ApiModelProperty(name = "verifyTimeMillis", value = "识别时间戳")
    private Long verifyTimeMillis;

    @ApiModelProperty(name = "verifyScore", value = "识别分数")
    private Integer verifyScore;

    @ApiModelProperty(name = "verifyResult", value = "识别结果：1-成功，2-失败")
    private Integer verifyResult;

    @ApiModelProperty(name = "verifyType", value = "入场方式：1-刷脸，2-刷卡")
    private Integer verifyType;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_APP_TYPE_KEY, value = "业务类型，1-门禁；1-签到；3-考勤；4-访客；5-团餐；6-温控")
    private Integer appType;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setSnapFaceBase64(String snapFaceBase64) {
        this.snapFaceBase64 = snapFaceBase64;
    }

    public void setSnapFaceUrl(String snapFaceUrl) {
        this.snapFaceUrl = snapFaceUrl;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public void setVerifyTimeMillis(Long verifyTimeMillis) {
        this.verifyTimeMillis = verifyTimeMillis;
    }

    public void setVerifyScore(Integer verifyScore) {
        this.verifyScore = verifyScore;
    }

    public void setVerifyResult(Integer verifyResult) {
        this.verifyResult = verifyResult;
    }

    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VerifyCallbackRequest)) {
            return false;
        }
        VerifyCallbackRequest other = (VerifyCallbackRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        if (this$deviceSn == null) {
            if (other$deviceSn != null) {
                return false;
            }
        } else if (!this$deviceSn.equals(other$deviceSn)) {
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
        Object this$snapFaceBase64 = getSnapFaceBase64();
        Object other$snapFaceBase64 = other.getSnapFaceBase64();
        if (this$snapFaceBase64 == null) {
            if (other$snapFaceBase64 != null) {
                return false;
            }
        } else if (!this$snapFaceBase64.equals(other$snapFaceBase64)) {
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
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        if (this$memberId == null) {
            if (other$memberId != null) {
                return false;
            }
        } else if (!this$memberId.equals(other$memberId)) {
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
        Object this$verifyTimeMillis = getVerifyTimeMillis();
        Object other$verifyTimeMillis = other.getVerifyTimeMillis();
        if (this$verifyTimeMillis == null) {
            if (other$verifyTimeMillis != null) {
                return false;
            }
        } else if (!this$verifyTimeMillis.equals(other$verifyTimeMillis)) {
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
        if (this$verifyResult == null) {
            if (other$verifyResult != null) {
                return false;
            }
        } else if (!this$verifyResult.equals(other$verifyResult)) {
            return false;
        }
        Object this$verifyType = getVerifyType();
        Object other$verifyType = other.getVerifyType();
        if (this$verifyType == null) {
            if (other$verifyType != null) {
                return false;
            }
        } else if (!this$verifyType.equals(other$verifyType)) {
            return false;
        }
        Object this$appType = getAppType();
        Object other$appType = other.getAppType();
        return this$appType == null ? other$appType == null : this$appType.equals(other$appType);
    }

    protected boolean canEqual(Object other) {
        return other instanceof VerifyCallbackRequest;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceName = getDeviceName();
        int result2 = (result * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $snapFaceBase64 = getSnapFaceBase64();
        int result3 = (result2 * 59) + ($snapFaceBase64 == null ? 43 : $snapFaceBase64.hashCode());
        Object $snapFaceUrl = getSnapFaceUrl();
        int result4 = (result3 * 59) + ($snapFaceUrl == null ? 43 : $snapFaceUrl.hashCode());
        Object $memberId = getMemberId();
        int result5 = (result4 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        int result6 = (result5 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $jobNum = getJobNum();
        int result7 = (result6 * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
        Object $verifyTimeMillis = getVerifyTimeMillis();
        int result8 = (result7 * 59) + ($verifyTimeMillis == null ? 43 : $verifyTimeMillis.hashCode());
        Object $verifyScore = getVerifyScore();
        int result9 = (result8 * 59) + ($verifyScore == null ? 43 : $verifyScore.hashCode());
        Object $verifyResult = getVerifyResult();
        int result10 = (result9 * 59) + ($verifyResult == null ? 43 : $verifyResult.hashCode());
        Object $verifyType = getVerifyType();
        int result11 = (result10 * 59) + ($verifyType == null ? 43 : $verifyType.hashCode());
        Object $appType = getAppType();
        return (result11 * 59) + ($appType == null ? 43 : $appType.hashCode());
    }

    public String toString() {
        return "VerifyCallbackRequest(deviceSn=" + getDeviceSn() + ", deviceName=" + getDeviceName() + ", snapFaceBase64=" + getSnapFaceBase64() + ", snapFaceUrl=" + getSnapFaceUrl() + ", memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", jobNum=" + getJobNum() + ", verifyTimeMillis=" + getVerifyTimeMillis() + ", verifyScore=" + getVerifyScore() + ", verifyResult=" + getVerifyResult() + ", verifyType=" + getVerifyType() + ", appType=" + getAppType() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getSnapFaceBase64() {
        return this.snapFaceBase64;
    }

    public String getSnapFaceUrl() {
        return this.snapFaceUrl;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getJobNum() {
        return this.jobNum;
    }

    public Long getVerifyTimeMillis() {
        return this.verifyTimeMillis;
    }

    public Integer getVerifyScore() {
        return this.verifyScore;
    }

    public Integer getVerifyResult() {
        return this.verifyResult;
    }

    public Integer getVerifyType() {
        return this.verifyType;
    }

    public Integer getAppType() {
        return this.appType;
    }
}
