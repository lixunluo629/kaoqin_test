package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "识别记录响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/WarnRecordResponse.class */
public class WarnRecordResponse implements Serializable {

    @ApiModelProperty(name = "warnRecordId", value = "报警记录id")
    private Long warnRecordId;

    @ApiModelProperty(name = "deviceSn", value = "设备串号")
    private String deviceSn;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "snapFaceUrl", value = "抓拍照url")
    private String snapFaceUrl;

    @ApiModelProperty(name = "warnType", value = "报警类型，类型未知，暂时直接存中文类型")
    private String warnType;

    @ApiModelProperty(name = "warnTime", value = "报警时间")
    private Date warnTime;

    public void setWarnRecordId(Long warnRecordId) {
        this.warnRecordId = warnRecordId;
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

    public void setWarnType(String warnType) {
        this.warnType = warnType;
    }

    public void setWarnTime(Date warnTime) {
        this.warnTime = warnTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WarnRecordResponse)) {
            return false;
        }
        WarnRecordResponse other = (WarnRecordResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$warnRecordId = getWarnRecordId();
        Object other$warnRecordId = other.getWarnRecordId();
        if (this$warnRecordId == null) {
            if (other$warnRecordId != null) {
                return false;
            }
        } else if (!this$warnRecordId.equals(other$warnRecordId)) {
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
        Object this$warnType = getWarnType();
        Object other$warnType = other.getWarnType();
        if (this$warnType == null) {
            if (other$warnType != null) {
                return false;
            }
        } else if (!this$warnType.equals(other$warnType)) {
            return false;
        }
        Object this$warnTime = getWarnTime();
        Object other$warnTime = other.getWarnTime();
        return this$warnTime == null ? other$warnTime == null : this$warnTime.equals(other$warnTime);
    }

    protected boolean canEqual(Object other) {
        return other instanceof WarnRecordResponse;
    }

    public int hashCode() {
        Object $warnRecordId = getWarnRecordId();
        int result = (1 * 59) + ($warnRecordId == null ? 43 : $warnRecordId.hashCode());
        Object $deviceSn = getDeviceSn();
        int result2 = (result * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceName = getDeviceName();
        int result3 = (result2 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $snapFaceUrl = getSnapFaceUrl();
        int result4 = (result3 * 59) + ($snapFaceUrl == null ? 43 : $snapFaceUrl.hashCode());
        Object $warnType = getWarnType();
        int result5 = (result4 * 59) + ($warnType == null ? 43 : $warnType.hashCode());
        Object $warnTime = getWarnTime();
        return (result5 * 59) + ($warnTime == null ? 43 : $warnTime.hashCode());
    }

    public String toString() {
        return "WarnRecordResponse(warnRecordId=" + getWarnRecordId() + ", deviceSn=" + getDeviceSn() + ", deviceName=" + getDeviceName() + ", snapFaceUrl=" + getSnapFaceUrl() + ", warnType=" + getWarnType() + ", warnTime=" + getWarnTime() + ")";
    }

    public Long getWarnRecordId() {
        return this.warnRecordId;
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

    public String getWarnType() {
        return this.warnType;
    }

    public Date getWarnTime() {
        return this.warnTime;
    }
}
