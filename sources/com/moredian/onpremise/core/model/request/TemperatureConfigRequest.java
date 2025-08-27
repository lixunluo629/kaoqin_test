package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.model.dto.TemperatureDeviceDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;

@ApiModel(description = "测温参数修改请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TemperatureConfigRequest.class */
public class TemperatureConfigRequest extends BaseRequest {
    private static final long serialVersionUID = -4673804018729927956L;

    @ApiModelProperty(name = "temperatureAlert", value = "预警温度值")
    private BigDecimal temperatureAlert;

    @ApiModelProperty(name = "openDoorFlag", value = "发热人员是否开门标志，1-开门；0-不开门")
    private Integer openDoorFlag;

    @ApiModelProperty(name = "strangerOpenDoorFlag", value = "陌生人是否开门标志，1-开门；0-不开门")
    private Integer strangerOpenDoorFlag;

    @ApiModelProperty(name = "devices", value = "测温设备列表")
    private List<TemperatureDeviceDto> devices;

    @ApiModelProperty(name = "manageDeviceSnList", value = "账户管理sn列表", hidden = true)
    private List<String> manageDeviceSnList;

    public void setTemperatureAlert(BigDecimal temperatureAlert) {
        this.temperatureAlert = temperatureAlert;
    }

    public void setOpenDoorFlag(Integer openDoorFlag) {
        this.openDoorFlag = openDoorFlag;
    }

    public void setStrangerOpenDoorFlag(Integer strangerOpenDoorFlag) {
        this.strangerOpenDoorFlag = strangerOpenDoorFlag;
    }

    public void setDevices(List<TemperatureDeviceDto> devices) {
        this.devices = devices;
    }

    public void setManageDeviceSnList(List<String> manageDeviceSnList) {
        this.manageDeviceSnList = manageDeviceSnList;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TemperatureConfigRequest)) {
            return false;
        }
        TemperatureConfigRequest other = (TemperatureConfigRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$devices = getDevices();
        Object other$devices = other.getDevices();
        if (this$devices == null) {
            if (other$devices != null) {
                return false;
            }
        } else if (!this$devices.equals(other$devices)) {
            return false;
        }
        Object this$manageDeviceSnList = getManageDeviceSnList();
        Object other$manageDeviceSnList = other.getManageDeviceSnList();
        return this$manageDeviceSnList == null ? other$manageDeviceSnList == null : this$manageDeviceSnList.equals(other$manageDeviceSnList);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof TemperatureConfigRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $temperatureAlert = getTemperatureAlert();
        int result = (1 * 59) + ($temperatureAlert == null ? 43 : $temperatureAlert.hashCode());
        Object $openDoorFlag = getOpenDoorFlag();
        int result2 = (result * 59) + ($openDoorFlag == null ? 43 : $openDoorFlag.hashCode());
        Object $strangerOpenDoorFlag = getStrangerOpenDoorFlag();
        int result3 = (result2 * 59) + ($strangerOpenDoorFlag == null ? 43 : $strangerOpenDoorFlag.hashCode());
        Object $devices = getDevices();
        int result4 = (result3 * 59) + ($devices == null ? 43 : $devices.hashCode());
        Object $manageDeviceSnList = getManageDeviceSnList();
        return (result4 * 59) + ($manageDeviceSnList == null ? 43 : $manageDeviceSnList.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "TemperatureConfigRequest(temperatureAlert=" + getTemperatureAlert() + ", openDoorFlag=" + getOpenDoorFlag() + ", strangerOpenDoorFlag=" + getStrangerOpenDoorFlag() + ", devices=" + getDevices() + ", manageDeviceSnList=" + getManageDeviceSnList() + ")";
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

    public List<TemperatureDeviceDto> getDevices() {
        return this.devices;
    }

    public List<String> getManageDeviceSnList() {
        return this.manageDeviceSnList;
    }
}
