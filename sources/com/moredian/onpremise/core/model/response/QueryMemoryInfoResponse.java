package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "内存信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/QueryMemoryInfoResponse.class */
public class QueryMemoryInfoResponse implements Serializable {
    private static final long serialVersionUID = -3983956832184799414L;

    @ApiModelProperty(name = "total", value = "总内存,单位K")
    private Long total;

    @ApiModelProperty(name = "free", value = "剩余内存,单位K")
    private Long free;

    public void setTotal(Long total) {
        this.total = total;
    }

    public void setFree(Long free) {
        this.free = free;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryMemoryInfoResponse)) {
            return false;
        }
        QueryMemoryInfoResponse other = (QueryMemoryInfoResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$total = getTotal();
        Object other$total = other.getTotal();
        if (this$total == null) {
            if (other$total != null) {
                return false;
            }
        } else if (!this$total.equals(other$total)) {
            return false;
        }
        Object this$free = getFree();
        Object other$free = other.getFree();
        return this$free == null ? other$free == null : this$free.equals(other$free);
    }

    protected boolean canEqual(Object other) {
        return other instanceof QueryMemoryInfoResponse;
    }

    public int hashCode() {
        Object $total = getTotal();
        int result = (1 * 59) + ($total == null ? 43 : $total.hashCode());
        Object $free = getFree();
        return (result * 59) + ($free == null ? 43 : $free.hashCode());
    }

    public String toString() {
        return "QueryMemoryInfoResponse(total=" + getTotal() + ", free=" + getFree() + ")";
    }

    public Long getTotal() {
        return this.total;
    }

    public Long getFree() {
        return this.free;
    }
}
