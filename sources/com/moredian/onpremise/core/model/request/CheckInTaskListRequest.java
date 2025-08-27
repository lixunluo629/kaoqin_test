package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "签到任务列表查询参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/CheckInTaskListRequest.class */
public class CheckInTaskListRequest extends BaseRequest {

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(name = "name", value = "任务名称，选填")
    private String name;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckInTaskListRequest)) {
            return false;
        }
        CheckInTaskListRequest other = (CheckInTaskListRequest) o;
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
        Object this$name = getName();
        Object other$name = other.getName();
        return this$name == null ? other$name == null : this$name.equals(other$name);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof CheckInTaskListRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $name = getName();
        return (result * 59) + ($name == null ? 43 : $name.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "CheckInTaskListRequest(paginator=" + getPaginator() + ", name=" + getName() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public String getName() {
        return this.name;
    }
}
