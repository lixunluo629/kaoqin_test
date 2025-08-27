package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "设备组请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeviceGroupRequest.class */
public class DeviceGroupRequest extends BaseRequest {
    private static final long serialVersionUID = -7572514175318689232L;

    @ApiModelProperty(name = "id", value = "id，更新时不可为空")
    private Long id;

    @ApiModelProperty(name = "name", value = "组名称，不可为空")
    private String name;

    @ApiModelProperty(name = "superId", value = "上级id，不可为空。新增时传0默认挂到根节点下,更新时传0默认保存在原来的父节点")
    private Long superId;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSuperId(Long superId) {
        this.superId = superId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceGroupRequest)) {
            return false;
        }
        DeviceGroupRequest other = (DeviceGroupRequest) o;
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
        return this$superId == null ? other$superId == null : this$superId.equals(other$superId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DeviceGroupRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $name = getName();
        int result2 = (result * 59) + ($name == null ? 43 : $name.hashCode());
        Object $superId = getSuperId();
        return (result2 * 59) + ($superId == null ? 43 : $superId.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DeviceGroupRequest(id=" + getId() + ", name=" + getName() + ", superId=" + getSuperId() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Long getSuperId() {
        return this.superId;
    }
}
