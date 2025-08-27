package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "设备列表请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeviceListRequest.class */
public class DeviceListRequest extends BaseRequest {
    private static final long serialVersionUID = -689176403638695344L;

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceType", value = "设备类型：1-门禁机，2-消费机-C，3-梯控机，4-自证机，5-消费机-A，6-消费机-W，7-销课机，8-温控机-A，9-温控机-B")
    private Integer deviceType;

    @ApiModelProperty(name = "deviceTypes", value = "支持多设备类型查询，取值参考上面，例[1,2]。deviceType和deviceTypes两者都传，取合集")
    private List<Integer> deviceTypes;

    @ApiModelProperty(name = "keywords", value = "关键字搜索，支持名称和sn模糊查询")
    private String keywords;

    @ApiModelProperty(name = "onlineStatus", value = "状态：1-在线，2-离线")
    private Integer onlineStatus;

    @ApiModelProperty(name = "deviceModel", value = "设备型号，例：D2,D1,MS3")
    private String deviceModel;

    @ApiModelProperty(name = "groupName", value = "权限组名称")
    private String groupName;

    @ApiModelProperty(name = "deviceGroupId", value = "设备组id")
    private Long deviceGroupId;

    @ApiModelProperty(name = "needSubDevice", value = "是否需要子设备组下设备：1-是，0-否")
    private Integer needSubDevice;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceTypes(List<Integer> deviceTypes) {
        this.deviceTypes = deviceTypes;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setDeviceGroupId(Long deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }

    public void setNeedSubDevice(Integer needSubDevice) {
        this.needSubDevice = needSubDevice;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceListRequest)) {
            return false;
        }
        DeviceListRequest other = (DeviceListRequest) o;
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
        Object this$deviceName = getDeviceName();
        Object other$deviceName = other.getDeviceName();
        if (this$deviceName == null) {
            if (other$deviceName != null) {
                return false;
            }
        } else if (!this$deviceName.equals(other$deviceName)) {
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
        Object this$deviceTypes = getDeviceTypes();
        Object other$deviceTypes = other.getDeviceTypes();
        if (this$deviceTypes == null) {
            if (other$deviceTypes != null) {
                return false;
            }
        } else if (!this$deviceTypes.equals(other$deviceTypes)) {
            return false;
        }
        Object this$keywords = getKeywords();
        Object other$keywords = other.getKeywords();
        if (this$keywords == null) {
            if (other$keywords != null) {
                return false;
            }
        } else if (!this$keywords.equals(other$keywords)) {
            return false;
        }
        Object this$onlineStatus = getOnlineStatus();
        Object other$onlineStatus = other.getOnlineStatus();
        if (this$onlineStatus == null) {
            if (other$onlineStatus != null) {
                return false;
            }
        } else if (!this$onlineStatus.equals(other$onlineStatus)) {
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
        Object this$groupName = getGroupName();
        Object other$groupName = other.getGroupName();
        if (this$groupName == null) {
            if (other$groupName != null) {
                return false;
            }
        } else if (!this$groupName.equals(other$groupName)) {
            return false;
        }
        Object this$deviceGroupId = getDeviceGroupId();
        Object other$deviceGroupId = other.getDeviceGroupId();
        if (this$deviceGroupId == null) {
            if (other$deviceGroupId != null) {
                return false;
            }
        } else if (!this$deviceGroupId.equals(other$deviceGroupId)) {
            return false;
        }
        Object this$needSubDevice = getNeedSubDevice();
        Object other$needSubDevice = other.getNeedSubDevice();
        return this$needSubDevice == null ? other$needSubDevice == null : this$needSubDevice.equals(other$needSubDevice);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DeviceListRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $deviceName = getDeviceName();
        int result2 = (result * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $deviceSn = getDeviceSn();
        int result3 = (result2 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceType = getDeviceType();
        int result4 = (result3 * 59) + ($deviceType == null ? 43 : $deviceType.hashCode());
        Object $deviceTypes = getDeviceTypes();
        int result5 = (result4 * 59) + ($deviceTypes == null ? 43 : $deviceTypes.hashCode());
        Object $keywords = getKeywords();
        int result6 = (result5 * 59) + ($keywords == null ? 43 : $keywords.hashCode());
        Object $onlineStatus = getOnlineStatus();
        int result7 = (result6 * 59) + ($onlineStatus == null ? 43 : $onlineStatus.hashCode());
        Object $deviceModel = getDeviceModel();
        int result8 = (result7 * 59) + ($deviceModel == null ? 43 : $deviceModel.hashCode());
        Object $groupName = getGroupName();
        int result9 = (result8 * 59) + ($groupName == null ? 43 : $groupName.hashCode());
        Object $deviceGroupId = getDeviceGroupId();
        int result10 = (result9 * 59) + ($deviceGroupId == null ? 43 : $deviceGroupId.hashCode());
        Object $needSubDevice = getNeedSubDevice();
        return (result10 * 59) + ($needSubDevice == null ? 43 : $needSubDevice.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DeviceListRequest(paginator=" + getPaginator() + ", deviceName=" + getDeviceName() + ", deviceSn=" + getDeviceSn() + ", deviceType=" + getDeviceType() + ", deviceTypes=" + getDeviceTypes() + ", keywords=" + getKeywords() + ", onlineStatus=" + getOnlineStatus() + ", deviceModel=" + getDeviceModel() + ", groupName=" + getGroupName() + ", deviceGroupId=" + getDeviceGroupId() + ", needSubDevice=" + getNeedSubDevice() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Integer getDeviceType() {
        return this.deviceType;
    }

    public List<Integer> getDeviceTypes() {
        return this.deviceTypes;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public Integer getOnlineStatus() {
        return this.onlineStatus;
    }

    public String getDeviceModel() {
        return this.deviceModel;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Long getDeviceGroupId() {
        return this.deviceGroupId;
    }

    public Integer getNeedSubDevice() {
        return this.needSubDevice;
    }
}
