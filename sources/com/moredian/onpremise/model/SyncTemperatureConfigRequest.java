package com.moredian.onpremise.model;

import java.io.Serializable;
import java.math.BigDecimal;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/SyncTemperatureConfigRequest.class */
public class SyncTemperatureConfigRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.SYNC_TEMPERATURE_CONFIG_REQUEST;
    private Long lastModifyTime;
    private Long orgId;
    private BigDecimal temperatureAlert;
    private Integer openDoorFlag;
    private Integer strangerOpenDoorFlag;
    private Integer temperatureEnable;
    private BigDecimal temperatureFactor;

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setTemperatureAlert(BigDecimal temperatureAlert) {
        this.temperatureAlert = temperatureAlert;
    }

    public void setOpenDoorFlag(Integer openDoorFlag) {
        this.openDoorFlag = openDoorFlag;
    }

    public void setStrangerOpenDoorFlag(Integer strangerOpenDoorFlag) {
        this.strangerOpenDoorFlag = strangerOpenDoorFlag;
    }

    public void setTemperatureEnable(Integer temperatureEnable) {
        this.temperatureEnable = temperatureEnable;
    }

    public void setTemperatureFactor(BigDecimal temperatureFactor) {
        this.temperatureFactor = temperatureFactor;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SyncTemperatureConfigRequest)) {
            return false;
        }
        SyncTemperatureConfigRequest other = (SyncTemperatureConfigRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$lastModifyTime = getLastModifyTime();
        Object other$lastModifyTime = other.getLastModifyTime();
        if (this$lastModifyTime == null) {
            if (other$lastModifyTime != null) {
                return false;
            }
        } else if (!this$lastModifyTime.equals(other$lastModifyTime)) {
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
        Object this$temperatureAlert = getTemperatureAlert();
        Object other$temperatureAlert = other.getTemperatureAlert();
        if (this$temperatureAlert == null) {
            if (other$temperatureAlert != null) {
                return false;
            }
        } else if (!this$temperatureAlert.equals(other$temperatureAlert)) {
            return false;
        }
        Object this$openDoorFlag = getOpenDoorFlag();
        Object other$openDoorFlag = other.getOpenDoorFlag();
        if (this$openDoorFlag == null) {
            if (other$openDoorFlag != null) {
                return false;
            }
        } else if (!this$openDoorFlag.equals(other$openDoorFlag)) {
            return false;
        }
        Object this$strangerOpenDoorFlag = getStrangerOpenDoorFlag();
        Object other$strangerOpenDoorFlag = other.getStrangerOpenDoorFlag();
        if (this$strangerOpenDoorFlag == null) {
            if (other$strangerOpenDoorFlag != null) {
                return false;
            }
        } else if (!this$strangerOpenDoorFlag.equals(other$strangerOpenDoorFlag)) {
            return false;
        }
        Object this$temperatureEnable = getTemperatureEnable();
        Object other$temperatureEnable = other.getTemperatureEnable();
        if (this$temperatureEnable == null) {
            if (other$temperatureEnable != null) {
                return false;
            }
        } else if (!this$temperatureEnable.equals(other$temperatureEnable)) {
            return false;
        }
        Object this$temperatureFactor = getTemperatureFactor();
        Object other$temperatureFactor = other.getTemperatureFactor();
        return this$temperatureFactor == null ? other$temperatureFactor == null : this$temperatureFactor.equals(other$temperatureFactor);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SyncTemperatureConfigRequest;
    }

    public int hashCode() {
        Object $lastModifyTime = getLastModifyTime();
        int result = (1 * 59) + ($lastModifyTime == null ? 43 : $lastModifyTime.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $temperatureAlert = getTemperatureAlert();
        int result3 = (result2 * 59) + ($temperatureAlert == null ? 43 : $temperatureAlert.hashCode());
        Object $openDoorFlag = getOpenDoorFlag();
        int result4 = (result3 * 59) + ($openDoorFlag == null ? 43 : $openDoorFlag.hashCode());
        Object $strangerOpenDoorFlag = getStrangerOpenDoorFlag();
        int result5 = (result4 * 59) + ($strangerOpenDoorFlag == null ? 43 : $strangerOpenDoorFlag.hashCode());
        Object $temperatureEnable = getTemperatureEnable();
        int result6 = (result5 * 59) + ($temperatureEnable == null ? 43 : $temperatureEnable.hashCode());
        Object $temperatureFactor = getTemperatureFactor();
        return (result6 * 59) + ($temperatureFactor == null ? 43 : $temperatureFactor.hashCode());
    }

    public String toString() {
        return "SyncTemperatureConfigRequest(lastModifyTime=" + getLastModifyTime() + ", orgId=" + getOrgId() + ", temperatureAlert=" + getTemperatureAlert() + ", openDoorFlag=" + getOpenDoorFlag() + ", strangerOpenDoorFlag=" + getStrangerOpenDoorFlag() + ", temperatureEnable=" + getTemperatureEnable() + ", temperatureFactor=" + getTemperatureFactor() + ")";
    }

    public Long getLastModifyTime() {
        return this.lastModifyTime;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public BigDecimal getTemperatureAlert() {
        return this.temperatureAlert;
    }

    public Integer getOpenDoorFlag() {
        return this.openDoorFlag;
    }

    public Integer getStrangerOpenDoorFlag() {
        return this.strangerOpenDoorFlag;
    }

    public Integer getTemperatureEnable() {
        return this.temperatureEnable;
    }

    public BigDecimal getTemperatureFactor() {
        return this.temperatureFactor;
    }

    public SyncTemperatureConfigRequest() {
    }

    public SyncTemperatureConfigRequest(Long lastModifyTime, Long orgId, Integer openDoorFlag, Integer strangerOpenDoorFlag, Integer temperatureEnable, BigDecimal temperatureAlert, BigDecimal temperatureFactor) {
        this.lastModifyTime = lastModifyTime;
        this.orgId = orgId;
        this.temperatureAlert = temperatureAlert;
        this.openDoorFlag = openDoorFlag;
        this.strangerOpenDoorFlag = strangerOpenDoorFlag;
        this.temperatureEnable = temperatureEnable;
        this.temperatureFactor = temperatureFactor;
    }
}
