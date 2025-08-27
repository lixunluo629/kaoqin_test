package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "设备组响应")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/DeviceGroupResponse.class */
public class DeviceGroupResponse implements Serializable {
    private static final long serialVersionUID = -1778510162774101722L;

    @ApiModelProperty(name = "id", value = "id")
    private Long id;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "name", value = "组名称")
    private String name;

    @ApiModelProperty(name = "superId", value = "上级id")
    private Long superId;

    @ApiModelProperty(name = "childDeviceGroups", value = "下级设备组")
    private List<DeviceGroupResponse> childDeviceGroups;

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSuperId(Long superId) {
        this.superId = superId;
    }

    public void setChildDeviceGroups(List<DeviceGroupResponse> childDeviceGroups) {
        this.childDeviceGroups = childDeviceGroups;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceGroupResponse)) {
            return false;
        }
        DeviceGroupResponse other = (DeviceGroupResponse) o;
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
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name == null) {
            if (other$name != null) {
                return false;
            }
        } else if (!this$name.equals(other$name)) {
            return false;
        }
        Object this$superId = getSuperId();
        Object other$superId = other.getSuperId();
        if (this$superId == null) {
            if (other$superId != null) {
                return false;
            }
        } else if (!this$superId.equals(other$superId)) {
            return false;
        }
        Object this$childDeviceGroups = getChildDeviceGroups();
        Object other$childDeviceGroups = other.getChildDeviceGroups();
        return this$childDeviceGroups == null ? other$childDeviceGroups == null : this$childDeviceGroups.equals(other$childDeviceGroups);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeviceGroupResponse;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $name = getName();
        int result3 = (result2 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $superId = getSuperId();
        int result4 = (result3 * 59) + ($superId == null ? 43 : $superId.hashCode());
        Object $childDeviceGroups = getChildDeviceGroups();
        return (result4 * 59) + ($childDeviceGroups == null ? 43 : $childDeviceGroups.hashCode());
    }

    public String toString() {
        return "DeviceGroupResponse(id=" + getId() + ", orgId=" + getOrgId() + ", name=" + getName() + ", superId=" + getSuperId() + ", childDeviceGroups=" + getChildDeviceGroups() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getName() {
        return this.name;
    }

    public Long getSuperId() {
        return this.superId;
    }

    public List<DeviceGroupResponse> getChildDeviceGroups() {
        return this.childDeviceGroups;
    }
}
