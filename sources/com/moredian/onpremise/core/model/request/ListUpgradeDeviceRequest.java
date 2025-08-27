package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "可升级设备列表查询")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ListUpgradeDeviceRequest.class */
public class ListUpgradeDeviceRequest extends BaseRequest {
    private static final long serialVersionUID = -4769170023872859497L;

    @ApiModelProperty(name = "packagePath", value = "升级包路径")
    private String packagePath;

    @ApiModelProperty(name = "searchKey", value = "设备名称或设备sn")
    private String searchKey;

    @ApiModelProperty(name = "deviceType", value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(name = "deviceGroupId", value = "设备组id")
    private Long deviceGroupId;

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceGroupId(Long deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListUpgradeDeviceRequest)) {
            return false;
        }
        ListUpgradeDeviceRequest other = (ListUpgradeDeviceRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$packagePath = getPackagePath();
        Object other$packagePath = other.getPackagePath();
        if (this$packagePath == null) {
            if (other$packagePath != null) {
                return false;
            }
        } else if (!this$packagePath.equals(other$packagePath)) {
            return false;
        }
        Object this$searchKey = getSearchKey();
        Object other$searchKey = other.getSearchKey();
        if (this$searchKey == null) {
            if (other$searchKey != null) {
                return false;
            }
        } else if (!this$searchKey.equals(other$searchKey)) {
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
        Object this$deviceGroupId = getDeviceGroupId();
        Object other$deviceGroupId = other.getDeviceGroupId();
        return this$deviceGroupId == null ? other$deviceGroupId == null : this$deviceGroupId.equals(other$deviceGroupId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ListUpgradeDeviceRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $packagePath = getPackagePath();
        int result = (1 * 59) + ($packagePath == null ? 43 : $packagePath.hashCode());
        Object $searchKey = getSearchKey();
        int result2 = (result * 59) + ($searchKey == null ? 43 : $searchKey.hashCode());
        Object $deviceType = getDeviceType();
        int result3 = (result2 * 59) + ($deviceType == null ? 43 : $deviceType.hashCode());
        Object $deviceGroupId = getDeviceGroupId();
        return (result3 * 59) + ($deviceGroupId == null ? 43 : $deviceGroupId.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ListUpgradeDeviceRequest(packagePath=" + getPackagePath() + ", searchKey=" + getSearchKey() + ", deviceType=" + getDeviceType() + ", deviceGroupId=" + getDeviceGroupId() + ")";
    }

    public String getPackagePath() {
        return this.packagePath;
    }

    public String getSearchKey() {
        return this.searchKey;
    }

    public Integer getDeviceType() {
        return this.deviceType;
    }

    public Long getDeviceGroupId() {
        return this.deviceGroupId;
    }
}
