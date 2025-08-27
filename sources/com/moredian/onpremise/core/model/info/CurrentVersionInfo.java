package com.moredian.onpremise.core.model.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "当前服务端版本信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/info/CurrentVersionInfo.class */
public class CurrentVersionInfo implements Serializable {

    @ApiModelProperty(name = "currentVersion", value = "当前版本")
    private String currentVersion;

    @ApiModelProperty(name = "expireTime", value = "过期时间")
    private Long expireTime;

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CurrentVersionInfo)) {
            return false;
        }
        CurrentVersionInfo other = (CurrentVersionInfo) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$currentVersion = getCurrentVersion();
        Object other$currentVersion = other.getCurrentVersion();
        if (this$currentVersion == null) {
            if (other$currentVersion != null) {
                return false;
            }
        } else if (!this$currentVersion.equals(other$currentVersion)) {
            return false;
        }
        Object this$expireTime = getExpireTime();
        Object other$expireTime = other.getExpireTime();
        return this$expireTime == null ? other$expireTime == null : this$expireTime.equals(other$expireTime);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CurrentVersionInfo;
    }

    public int hashCode() {
        Object $currentVersion = getCurrentVersion();
        int result = (1 * 59) + ($currentVersion == null ? 43 : $currentVersion.hashCode());
        Object $expireTime = getExpireTime();
        return (result * 59) + ($expireTime == null ? 43 : $expireTime.hashCode());
    }

    public String toString() {
        return "CurrentVersionInfo(currentVersion=" + getCurrentVersion() + ", expireTime=" + getExpireTime() + ")";
    }

    public String getCurrentVersion() {
        return this.currentVersion;
    }

    public Long getExpireTime() {
        return this.expireTime;
    }
}
