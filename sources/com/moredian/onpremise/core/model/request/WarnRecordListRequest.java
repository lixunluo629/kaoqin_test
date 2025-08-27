package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "报警记录查询列表请求信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/WarnRecordListRequest.class */
public class WarnRecordListRequest extends BaseRequest {

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(name = "startTimeStr", value = "开始时间字符串")
    private String startTimeStr;

    @ApiModelProperty(name = "endTimeStr", value = "结束时间字符串")
    private String endTimeStr;

    @ApiModelProperty(name = "deviceId", value = "设备id")
    private Long deviceId;

    @ApiModelProperty(name = "deviceSn", value = "设备Sn")
    private String deviceSn;

    @ApiModelProperty(name = "warnType", value = "报警类型")
    private String warnType;

    @ApiModelProperty(name = "warnTypeCode", value = "报警类型，1-开门超时报警；2-非法开门报警；3-火警；4-防拆报警；5-红外攻击；6-闯入告警；7-连续进场;8-连续出场")
    private Integer warnTypeCode;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setWarnType(String warnType) {
        this.warnType = warnType;
    }

    public void setWarnTypeCode(Integer warnTypeCode) {
        this.warnTypeCode = warnTypeCode;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WarnRecordListRequest)) {
            return false;
        }
        WarnRecordListRequest other = (WarnRecordListRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$paginator = getPaginator();
        Object other$paginator = other.getPaginator();
        if (this$paginator == null) {
            if (other$paginator != null) {
                return false;
            }
        } else if (!this$paginator.equals(other$paginator)) {
            return false;
        }
        Object this$startTimeStr = getStartTimeStr();
        Object other$startTimeStr = other.getStartTimeStr();
        if (this$startTimeStr == null) {
            if (other$startTimeStr != null) {
                return false;
            }
        } else if (!this$startTimeStr.equals(other$startTimeStr)) {
            return false;
        }
        Object this$endTimeStr = getEndTimeStr();
        Object other$endTimeStr = other.getEndTimeStr();
        if (this$endTimeStr == null) {
            if (other$endTimeStr != null) {
                return false;
            }
        } else if (!this$endTimeStr.equals(other$endTimeStr)) {
            return false;
        }
        Object this$deviceId = getDeviceId();
        Object other$deviceId = other.getDeviceId();
        if (this$deviceId == null) {
            if (other$deviceId != null) {
                return false;
            }
        } else if (!this$deviceId.equals(other$deviceId)) {
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
        Object this$warnType = getWarnType();
        Object other$warnType = other.getWarnType();
        if (this$warnType == null) {
            if (other$warnType != null) {
                return false;
            }
        } else if (!this$warnType.equals(other$warnType)) {
            return false;
        }
        Object this$warnTypeCode = getWarnTypeCode();
        Object other$warnTypeCode = other.getWarnTypeCode();
        return this$warnTypeCode == null ? other$warnTypeCode == null : this$warnTypeCode.equals(other$warnTypeCode);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof WarnRecordListRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $startTimeStr = getStartTimeStr();
        int result2 = (result * 59) + ($startTimeStr == null ? 43 : $startTimeStr.hashCode());
        Object $endTimeStr = getEndTimeStr();
        int result3 = (result2 * 59) + ($endTimeStr == null ? 43 : $endTimeStr.hashCode());
        Object $deviceId = getDeviceId();
        int result4 = (result3 * 59) + ($deviceId == null ? 43 : $deviceId.hashCode());
        Object $deviceSn = getDeviceSn();
        int result5 = (result4 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $warnType = getWarnType();
        int result6 = (result5 * 59) + ($warnType == null ? 43 : $warnType.hashCode());
        Object $warnTypeCode = getWarnTypeCode();
        return (result6 * 59) + ($warnTypeCode == null ? 43 : $warnTypeCode.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "WarnRecordListRequest(paginator=" + getPaginator() + ", startTimeStr=" + getStartTimeStr() + ", endTimeStr=" + getEndTimeStr() + ", deviceId=" + getDeviceId() + ", deviceSn=" + getDeviceSn() + ", warnType=" + getWarnType() + ", warnTypeCode=" + getWarnTypeCode() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public String getStartTimeStr() {
        return this.startTimeStr;
    }

    public String getEndTimeStr() {
        return this.endTimeStr;
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getWarnType() {
        return this.warnType;
    }

    public Integer getWarnTypeCode() {
        return this.warnTypeCode;
    }
}
