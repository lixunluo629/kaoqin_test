package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "报警回调通知参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/WarnCallbackRequest.class */
public class WarnCallbackRequest implements Serializable {
    private static final long serialVersionUID = -4826093329843697005L;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "snapFaceUrl", value = "抓拍照url")
    private String snapFaceUrl;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "jobNum", value = "成员工号")
    private String jobNum;

    @ApiModelProperty(name = "warnTimeMillis", value = "报警时间戳")
    private Long warnTimeMillis;

    @ApiModelProperty(name = "warnTypeCode", value = "报警类型code；1-开门超时报警；2-非法开门报警；3-火警；4-防拆报警；5-红外攻击；6-闯入告警；7-连续进场;8-连续出场")
    private Integer warnTypeCode;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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

    public void setWarnTimeMillis(Long warnTimeMillis) {
        this.warnTimeMillis = warnTimeMillis;
    }

    public void setWarnTypeCode(Integer warnTypeCode) {
        this.warnTypeCode = warnTypeCode;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WarnCallbackRequest)) {
            return false;
        }
        WarnCallbackRequest other = (WarnCallbackRequest) o;
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
        Object this$warnTimeMillis = getWarnTimeMillis();
        Object other$warnTimeMillis = other.getWarnTimeMillis();
        if (this$warnTimeMillis == null) {
            if (other$warnTimeMillis != null) {
                return false;
            }
        } else if (!this$warnTimeMillis.equals(other$warnTimeMillis)) {
            return false;
        }
        Object this$warnTypeCode = getWarnTypeCode();
        Object other$warnTypeCode = other.getWarnTypeCode();
        return this$warnTypeCode == null ? other$warnTypeCode == null : this$warnTypeCode.equals(other$warnTypeCode);
    }

    protected boolean canEqual(Object other) {
        return other instanceof WarnCallbackRequest;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceName = getDeviceName();
        int result2 = (result * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $snapFaceUrl = getSnapFaceUrl();
        int result3 = (result2 * 59) + ($snapFaceUrl == null ? 43 : $snapFaceUrl.hashCode());
        Object $memberId = getMemberId();
        int result4 = (result3 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        int result5 = (result4 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $jobNum = getJobNum();
        int result6 = (result5 * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
        Object $warnTimeMillis = getWarnTimeMillis();
        int result7 = (result6 * 59) + ($warnTimeMillis == null ? 43 : $warnTimeMillis.hashCode());
        Object $warnTypeCode = getWarnTypeCode();
        return (result7 * 59) + ($warnTypeCode == null ? 43 : $warnTypeCode.hashCode());
    }

    public String toString() {
        return "WarnCallbackRequest(deviceSn=" + getDeviceSn() + ", deviceName=" + getDeviceName() + ", snapFaceUrl=" + getSnapFaceUrl() + ", memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", jobNum=" + getJobNum() + ", warnTimeMillis=" + getWarnTimeMillis() + ", warnTypeCode=" + getWarnTypeCode() + ")";
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

    public Long getMemberId() {
        return this.memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getJobNum() {
        return this.jobNum;
    }

    public Long getWarnTimeMillis() {
        return this.warnTimeMillis;
    }

    public Integer getWarnTypeCode() {
        return this.warnTypeCode;
    }
}
