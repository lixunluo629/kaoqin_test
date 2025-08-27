package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "页面配置请求对象")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ViewConfigListRequest.class */
public class ViewConfigListRequest extends BaseRequest {
    private static final long serialVersionUID = -5335666469257402273L;

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(name = "type", value = "类型：1-登录页；2-app模块；3-文字替换")
    private Integer type;

    @ApiModelProperty(name = "bizKey", value = "", hidden = true)
    private String bizKey;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setBizKey(String bizKey) {
        this.bizKey = bizKey;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ViewConfigListRequest)) {
            return false;
        }
        ViewConfigListRequest other = (ViewConfigListRequest) o;
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
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type == null) {
            if (other$type != null) {
                return false;
            }
        } else if (!this$type.equals(other$type)) {
            return false;
        }
        Object this$bizKey = getBizKey();
        Object other$bizKey = other.getBizKey();
        return this$bizKey == null ? other$bizKey == null : this$bizKey.equals(other$bizKey);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ViewConfigListRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $type = getType();
        int result2 = (result * 59) + ($type == null ? 43 : $type.hashCode());
        Object $bizKey = getBizKey();
        return (result2 * 59) + ($bizKey == null ? 43 : $bizKey.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ViewConfigListRequest(paginator=" + getPaginator() + ", type=" + getType() + ", bizKey=" + getBizKey() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public Integer getType() {
        return this.type;
    }

    public String getBizKey() {
        return this.bizKey;
    }
}
