package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "授权码列表请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ListAuthCodeRequest.class */
public class ListAuthCodeRequest extends BaseRequest {

    @ApiModelProperty(name = "paginator", value = "分页对象")
    private Paginator paginator;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListAuthCodeRequest)) {
            return false;
        }
        ListAuthCodeRequest other = (ListAuthCodeRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$paginator = getPaginator();
        Object other$paginator = other.getPaginator();
        return this$paginator == null ? other$paginator == null : this$paginator.equals(other$paginator);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ListAuthCodeRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ListAuthCodeRequest(paginator=" + getPaginator() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }
}
