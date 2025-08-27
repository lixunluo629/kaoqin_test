package com.moredian.onpremise.core.model.info;

import com.moredian.onpremise.core.model.dto.DeviceDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "更新权限组设备")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/info/UpdateGroupDeviceInfo.class */
public class UpdateGroupDeviceInfo implements Serializable {
    private static final long serialVersionUID = -4429678747169736156L;

    @ApiModelProperty(name = "newDevices", value = "新绑定设备列表")
    private List<DeviceDto> newDevices;

    @ApiModelProperty(name = "oldDevices", value = "原绑定设备列表")
    private List<DeviceDto> oldDevices;

    public void setNewDevices(List<DeviceDto> newDevices) {
        this.newDevices = newDevices;
    }

    public void setOldDevices(List<DeviceDto> oldDevices) {
        this.oldDevices = oldDevices;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateGroupDeviceInfo)) {
            return false;
        }
        UpdateGroupDeviceInfo other = (UpdateGroupDeviceInfo) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$newDevices = getNewDevices();
        Object other$newDevices = other.getNewDevices();
        if (this$newDevices == null) {
            if (other$newDevices != null) {
                return false;
            }
        } else if (!this$newDevices.equals(other$newDevices)) {
            return false;
        }
        Object this$oldDevices = getOldDevices();
        Object other$oldDevices = other.getOldDevices();
        return this$oldDevices == null ? other$oldDevices == null : this$oldDevices.equals(other$oldDevices);
    }

    protected boolean canEqual(Object other) {
        return other instanceof UpdateGroupDeviceInfo;
    }

    public int hashCode() {
        Object $newDevices = getNewDevices();
        int result = (1 * 59) + ($newDevices == null ? 43 : $newDevices.hashCode());
        Object $oldDevices = getOldDevices();
        return (result * 59) + ($oldDevices == null ? 43 : $oldDevices.hashCode());
    }

    public String toString() {
        return "UpdateGroupDeviceInfo(newDevices=" + getNewDevices() + ", oldDevices=" + getOldDevices() + ")";
    }

    public List<DeviceDto> getNewDevices() {
        return this.newDevices;
    }

    public List<DeviceDto> getOldDevices() {
        return this.oldDevices;
    }

    public UpdateGroupDeviceInfo() {
    }

    public UpdateGroupDeviceInfo(List<DeviceDto> newDevices, List<DeviceDto> oldDevices) {
        this.newDevices = newDevices;
        this.oldDevices = oldDevices;
    }
}
