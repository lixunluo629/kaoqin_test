package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "设备组删除请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeviceGroupDeleteRequest.class */
public class DeviceGroupDeleteRequest extends BaseRequest {
    private static final long serialVersionUID = -8824248175813497541L;

    @ApiModelProperty(name = "id", value = "id，不可为空")
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceGroupDeleteRequest)) {
            return false;
        }
        DeviceGroupDeleteRequest other = (DeviceGroupDeleteRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        return this$id == null ? other$id == null : this$id.equals(other$id);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DeviceGroupDeleteRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DeviceGroupDeleteRequest(id=" + getId() + ")";
    }

    public Long getId() {
        return this.id;
    }
}
