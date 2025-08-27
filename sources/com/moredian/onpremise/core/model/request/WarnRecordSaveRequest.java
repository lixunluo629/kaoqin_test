package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "报警识别记录保存请求对象")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/WarnRecordSaveRequest.class */
public class WarnRecordSaveRequest implements Serializable {

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "snapFaceBase64", value = "抓拍照base64")
    private String snapFaceBase64;

    @ApiModelProperty(name = "warnTypeCode", value = "报警类型，1-开门超时报警；2-非法开门报警；3-火警；4-防拆报警；5-红外攻击；6-闯入告警；7-连续进场;8-连续出场")
    private Integer warnTypeCode;

    @ApiModelProperty(name = "warnType", value = "报警类型")
    private String warnType;

    @ApiModelProperty(name = "warnTime", value = "报警时间")
    private Long warnTime;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setSnapFaceBase64(String snapFaceBase64) {
        this.snapFaceBase64 = snapFaceBase64;
    }

    public void setWarnTypeCode(Integer warnTypeCode) {
        this.warnTypeCode = warnTypeCode;
    }

    public void setWarnType(String warnType) {
        this.warnType = warnType;
    }

    public void setWarnTime(Long warnTime) {
        this.warnTime = warnTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WarnRecordSaveRequest)) {
            return false;
        }
        WarnRecordSaveRequest other = (WarnRecordSaveRequest) o;
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
        Object this$snapFaceBase64 = getSnapFaceBase64();
        Object other$snapFaceBase64 = other.getSnapFaceBase64();
        if (this$snapFaceBase64 == null) {
            if (other$snapFaceBase64 != null) {
                return false;
            }
        } else if (!this$snapFaceBase64.equals(other$snapFaceBase64)) {
            return false;
        }
        Object this$warnTypeCode = getWarnTypeCode();
        Object other$warnTypeCode = other.getWarnTypeCode();
        if (this$warnTypeCode == null) {
            if (other$warnTypeCode != null) {
                return false;
            }
        } else if (!this$warnTypeCode.equals(other$warnTypeCode)) {
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
        return other instanceof WarnRecordSaveRequest;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $snapFaceBase64 = getSnapFaceBase64();
        int result2 = (result * 59) + ($snapFaceBase64 == null ? 43 : $snapFaceBase64.hashCode());
        Object $warnTypeCode = getWarnTypeCode();
        int result3 = (result2 * 59) + ($warnTypeCode == null ? 43 : $warnTypeCode.hashCode());
        Object $warnType = getWarnType();
        int result4 = (result3 * 59) + ($warnType == null ? 43 : $warnType.hashCode());
        Object $warnTime = getWarnTime();
        return (result4 * 59) + ($warnTime == null ? 43 : $warnTime.hashCode());
    }

    public String toString() {
        return "WarnRecordSaveRequest(deviceSn=" + getDeviceSn() + ", snapFaceBase64=" + getSnapFaceBase64() + ", warnTypeCode=" + getWarnTypeCode() + ", warnType=" + getWarnType() + ", warnTime=" + getWarnTime() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getSnapFaceBase64() {
        return this.snapFaceBase64;
    }

    public Integer getWarnTypeCode() {
        return this.warnTypeCode;
    }

    public String getWarnType() {
        return this.warnType;
    }

    public Long getWarnTime() {
        return this.warnTime;
    }
}
