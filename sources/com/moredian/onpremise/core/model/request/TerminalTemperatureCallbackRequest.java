package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(description = "终端测温记录回调请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalTemperatureCallbackRequest.class */
public class TerminalTemperatureCallbackRequest implements Serializable {
    private static final long serialVersionUID = -7928840192203064643L;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "jobNum", value = "成员工号")
    private String jobNum;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "snapFaceUrl", value = "抓拍照url")
    private String snapFaceUrl;

    @ApiModelProperty(name = "verifyTimeMillis", value = "识别时间戳")
    private Long verifyTimeMillis;

    @ApiModelProperty(name = "temperatureValue", value = "温度值")
    private BigDecimal temperatureValue;

    @ApiModelProperty(name = "temperatureStatus", value = "温度状态：1-正常，2-高温")
    private Integer temperatureStatus;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setSnapFaceUrl(String snapFaceUrl) {
        this.snapFaceUrl = snapFaceUrl;
    }

    public void setVerifyTimeMillis(Long verifyTimeMillis) {
        this.verifyTimeMillis = verifyTimeMillis;
    }

    public void setTemperatureValue(BigDecimal temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public void setTemperatureStatus(Integer temperatureStatus) {
        this.temperatureStatus = temperatureStatus;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalTemperatureCallbackRequest)) {
            return false;
        }
        TerminalTemperatureCallbackRequest other = (TerminalTemperatureCallbackRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$snapFaceUrl = getSnapFaceUrl();
        Object other$snapFaceUrl = other.getSnapFaceUrl();
        if (this$snapFaceUrl == null) {
            if (other$snapFaceUrl != null) {
                return false;
            }
        } else if (!this$snapFaceUrl.equals(other$snapFaceUrl)) {
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
        Object this$temperatureValue = getTemperatureValue();
        Object other$temperatureValue = other.getTemperatureValue();
        if (this$temperatureValue == null) {
            if (other$temperatureValue != null) {
                return false;
            }
        } else if (!this$temperatureValue.equals(other$temperatureValue)) {
            return false;
        }
        Object this$temperatureStatus = getTemperatureStatus();
        Object other$temperatureStatus = other.getTemperatureStatus();
        return this$temperatureStatus == null ? other$temperatureStatus == null : this$temperatureStatus.equals(other$temperatureStatus);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalTemperatureCallbackRequest;
    }

    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        int result2 = (result * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $jobNum = getJobNum();
        int result3 = (result2 * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
        Object $deviceSn = getDeviceSn();
        int result4 = (result3 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceName = getDeviceName();
        int result5 = (result4 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $snapFaceUrl = getSnapFaceUrl();
        int result6 = (result5 * 59) + ($snapFaceUrl == null ? 43 : $snapFaceUrl.hashCode());
        Object $verifyTimeMillis = getVerifyTimeMillis();
        int result7 = (result6 * 59) + ($verifyTimeMillis == null ? 43 : $verifyTimeMillis.hashCode());
        Object $temperatureValue = getTemperatureValue();
        int result8 = (result7 * 59) + ($temperatureValue == null ? 43 : $temperatureValue.hashCode());
        Object $temperatureStatus = getTemperatureStatus();
        return (result8 * 59) + ($temperatureStatus == null ? 43 : $temperatureStatus.hashCode());
    }

    public String toString() {
        return "TerminalTemperatureCallbackRequest(memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", jobNum=" + getJobNum() + ", deviceSn=" + getDeviceSn() + ", deviceName=" + getDeviceName() + ", snapFaceUrl=" + getSnapFaceUrl() + ", verifyTimeMillis=" + getVerifyTimeMillis() + ", temperatureValue=" + getTemperatureValue() + ", temperatureStatus=" + getTemperatureStatus() + ")";
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

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getSnapFaceUrl() {
        return this.snapFaceUrl;
    }

    public Long getVerifyTimeMillis() {
        return this.verifyTimeMillis;
    }

    public BigDecimal getTemperatureValue() {
        return this.temperatureValue;
    }

    public Integer getTemperatureStatus() {
        return this.temperatureStatus;
    }
}
