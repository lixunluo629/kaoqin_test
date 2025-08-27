package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.model.dto.DeviceDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "访客设置关联设备参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/VisitConfigDeviceRequest.class */
public class VisitConfigDeviceRequest extends BaseRequest {
    private static final long serialVersionUID = -4511815973444728503L;

    @ApiModelProperty(name = "visitConfigId", value = "访客配置id")
    private Long visitConfigId;

    @ApiModelProperty(name = "deviceSns", value = "设备sn")
    private List<DeviceDto> deviceSns;

    public void setVisitConfigId(Long visitConfigId) {
        this.visitConfigId = visitConfigId;
    }

    public void setDeviceSns(List<DeviceDto> deviceSns) {
        this.deviceSns = deviceSns;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VisitConfigDeviceRequest)) {
            return false;
        }
        VisitConfigDeviceRequest other = (VisitConfigDeviceRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$visitConfigId = getVisitConfigId();
        Object other$visitConfigId = other.getVisitConfigId();
        if (this$visitConfigId == null) {
            if (other$visitConfigId != null) {
                return false;
            }
        } else if (!this$visitConfigId.equals(other$visitConfigId)) {
            return false;
        }
        Object this$deviceSns = getDeviceSns();
        Object other$deviceSns = other.getDeviceSns();
        return this$deviceSns == null ? other$deviceSns == null : this$deviceSns.equals(other$deviceSns);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof VisitConfigDeviceRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $visitConfigId = getVisitConfigId();
        int result = (1 * 59) + ($visitConfigId == null ? 43 : $visitConfigId.hashCode());
        Object $deviceSns = getDeviceSns();
        return (result * 59) + ($deviceSns == null ? 43 : $deviceSns.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "VisitConfigDeviceRequest(visitConfigId=" + getVisitConfigId() + ", deviceSns=" + getDeviceSns() + ")";
    }

    public Long getVisitConfigId() {
        return this.visitConfigId;
    }

    public List<DeviceDto> getDeviceSns() {
        return this.deviceSns;
    }
}
