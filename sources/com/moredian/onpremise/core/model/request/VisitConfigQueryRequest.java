package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "访客设置查询参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/VisitConfigQueryRequest.class */
public class VisitConfigQueryRequest extends BaseRequest {
    private static final long serialVersionUID = -3357829525025118306L;

    @ApiModelProperty(name = "id", value = "更新时必填")
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VisitConfigQueryRequest)) {
            return false;
        }
        VisitConfigQueryRequest other = (VisitConfigQueryRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        return this$id == null ? other$id == null : this$id.equals(other$id);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof VisitConfigQueryRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "VisitConfigQueryRequest(id=" + getId() + ")";
    }

    public Long getId() {
        return this.id;
    }
}
