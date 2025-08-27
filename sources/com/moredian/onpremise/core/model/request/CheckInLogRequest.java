package com.moredian.onpremise.core.model.request;

import io.netty.handler.codec.rtsp.RtspHeaders;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "签到记录")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/CheckInLogRequest.class */
public class CheckInLogRequest implements Serializable {

    @ApiModelProperty(name = "id", value = "签到任务id")
    private Long id;

    @ApiModelProperty(name = "orgId", value = "组织id")
    private Long orgId;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = RtspHeaders.Values.URL, value = "抓拍照")
    private String url;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "checkInTime", value = "签到时间")
    private Long checkInTime;

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setCheckInTime(Long checkInTime) {
        this.checkInTime = checkInTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckInLogRequest)) {
            return false;
        }
        CheckInLogRequest other = (CheckInLogRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        if (this$id == null) {
            if (other$id != null) {
                return false;
            }
        } else if (!this$id.equals(other$id)) {
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
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        if (this$deviceSn == null) {
            if (other$deviceSn != null) {
                return false;
            }
        } else if (!this$deviceSn.equals(other$deviceSn)) {
            return false;
        }
        Object this$url = getUrl();
        Object other$url = other.getUrl();
        if (this$url == null) {
            if (other$url != null) {
                return false;
            }
        } else if (!this$url.equals(other$url)) {
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
        Object this$checkInTime = getCheckInTime();
        Object other$checkInTime = other.getCheckInTime();
        return this$checkInTime == null ? other$checkInTime == null : this$checkInTime.equals(other$checkInTime);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CheckInLogRequest;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $deviceSn = getDeviceSn();
        int result3 = (result2 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $url = getUrl();
        int result4 = (result3 * 59) + ($url == null ? 43 : $url.hashCode());
        Object $memberId = getMemberId();
        int result5 = (result4 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $checkInTime = getCheckInTime();
        return (result5 * 59) + ($checkInTime == null ? 43 : $checkInTime.hashCode());
    }

    public String toString() {
        return "CheckInLogRequest(id=" + getId() + ", orgId=" + getOrgId() + ", deviceSn=" + getDeviceSn() + ", url=" + getUrl() + ", memberId=" + getMemberId() + ", checkInTime=" + getCheckInTime() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getUrl() {
        return this.url;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public Long getCheckInTime() {
        return this.checkInTime;
    }
}
