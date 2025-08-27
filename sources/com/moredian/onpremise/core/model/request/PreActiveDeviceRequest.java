package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "设备预激活参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/PreActiveDeviceRequest.class */
public class PreActiveDeviceRequest extends BaseRequest {
    private static final long serialVersionUID = -1045214853230210964L;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceType", value = "设备类型：1-门禁机，2-消费机-C，3-梯控机，4-自证机，5-消费机-A，6-消费机-W，7-销课机，8-温控机-A，9-温控机-B")
    private Integer deviceType;

    @ApiModelProperty(name = "deviceModel", value = "设备型号")
    private String deviceModel;

    @ApiModelProperty(name = "fullDeviceGroupName", value = "设备组名称，上下级用‘-’分隔，例：要将设备激活在‘孙子设备组’时需填入：‘父设备组-子设备组-孙子设备组’")
    private String fullDeviceGroupName;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setFullDeviceGroupName(String fullDeviceGroupName) {
        this.fullDeviceGroupName = fullDeviceGroupName;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PreActiveDeviceRequest)) {
            return false;
        }
        PreActiveDeviceRequest other = (PreActiveDeviceRequest) o;
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
        Object this$deviceType = getDeviceType();
        Object other$deviceType = other.getDeviceType();
        if (this$deviceType == null) {
            if (other$deviceType != null) {
                return false;
            }
        } else if (!this$deviceType.equals(other$deviceType)) {
            return false;
        }
        Object this$deviceModel = getDeviceModel();
        Object other$deviceModel = other.getDeviceModel();
        if (this$deviceModel == null) {
            if (other$deviceModel != null) {
                return false;
            }
        } else if (!this$deviceModel.equals(other$deviceModel)) {
            return false;
        }
        Object this$fullDeviceGroupName = getFullDeviceGroupName();
        Object other$fullDeviceGroupName = other.getFullDeviceGroupName();
        return this$fullDeviceGroupName == null ? other$fullDeviceGroupName == null : this$fullDeviceGroupName.equals(other$fullDeviceGroupName);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof PreActiveDeviceRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceType = getDeviceType();
        int result2 = (result * 59) + ($deviceType == null ? 43 : $deviceType.hashCode());
        Object $deviceModel = getDeviceModel();
        int result3 = (result2 * 59) + ($deviceModel == null ? 43 : $deviceModel.hashCode());
        Object $fullDeviceGroupName = getFullDeviceGroupName();
        return (result3 * 59) + ($fullDeviceGroupName == null ? 43 : $fullDeviceGroupName.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "PreActiveDeviceRequest(deviceSn=" + getDeviceSn() + ", deviceType=" + getDeviceType() + ", deviceModel=" + getDeviceModel() + ", fullDeviceGroupName=" + getFullDeviceGroupName() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Integer getDeviceType() {
        return this.deviceType;
    }

    public String getDeviceModel() {
        return this.deviceModel;
    }

    public String getFullDeviceGroupName() {
        return this.fullDeviceGroupName;
    }
}
