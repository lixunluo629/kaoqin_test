package com.moredian.onpremise.core.model.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.web.servlet.tags.BindTag;

@ApiModel(description = "火警缓存状态信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/info/FireWarnStatusInfo.class */
public class FireWarnStatusInfo implements Serializable {

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "expireTime", value = "过期时间")
    private Long expireTime;

    @ApiModelProperty(name = BindTag.STATUS_VARIABLE_NAME, value = "状态：1：正常；2-火警")
    private Integer status;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FireWarnStatusInfo)) {
            return false;
        }
        FireWarnStatusInfo other = (FireWarnStatusInfo) o;
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
        Object this$expireTime = getExpireTime();
        Object other$expireTime = other.getExpireTime();
        if (this$expireTime == null) {
            if (other$expireTime != null) {
                return false;
            }
        } else if (!this$expireTime.equals(other$expireTime)) {
            return false;
        }
        Object this$status = getStatus();
        Object other$status = other.getStatus();
        return this$status == null ? other$status == null : this$status.equals(other$status);
    }

    protected boolean canEqual(Object other) {
        return other instanceof FireWarnStatusInfo;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $expireTime = getExpireTime();
        int result2 = (result * 59) + ($expireTime == null ? 43 : $expireTime.hashCode());
        Object $status = getStatus();
        return (result2 * 59) + ($status == null ? 43 : $status.hashCode());
    }

    public String toString() {
        return "FireWarnStatusInfo(deviceSn=" + getDeviceSn() + ", expireTime=" + getExpireTime() + ", status=" + getStatus() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Long getExpireTime() {
        return this.expireTime;
    }

    public Integer getStatus() {
        return this.status;
    }
}
