package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "操作日志查询列表请求信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/OperLogListRequest.class */
public class OperLogListRequest extends BaseRequest {
    private static final long serialVersionUID = -4099639642137255980L;

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(name = "startTimeStr", value = "开始时间字符串,\"yyyy-MM-dd\"")
    private String startTimeStr;

    @ApiModelProperty(name = "endTimeStr", value = "结束时间字符串,\"yyyy-MM-dd\"")
    private String endTimeStr;

    @ApiModelProperty(name = "accountNameLike", value = "账号名，模糊查询")
    private String accountNameLike;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public void setAccountNameLike(String accountNameLike) {
        this.accountNameLike = accountNameLike;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OperLogListRequest)) {
            return false;
        }
        OperLogListRequest other = (OperLogListRequest) o;
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
        Object this$startTimeStr = getStartTimeStr();
        Object other$startTimeStr = other.getStartTimeStr();
        if (this$startTimeStr == null) {
            if (other$startTimeStr != null) {
                return false;
            }
        } else if (!this$startTimeStr.equals(other$startTimeStr)) {
            return false;
        }
        Object this$endTimeStr = getEndTimeStr();
        Object other$endTimeStr = other.getEndTimeStr();
        if (this$endTimeStr == null) {
            if (other$endTimeStr != null) {
                return false;
            }
        } else if (!this$endTimeStr.equals(other$endTimeStr)) {
            return false;
        }
        Object this$accountNameLike = getAccountNameLike();
        Object other$accountNameLike = other.getAccountNameLike();
        return this$accountNameLike == null ? other$accountNameLike == null : this$accountNameLike.equals(other$accountNameLike);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof OperLogListRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $startTimeStr = getStartTimeStr();
        int result2 = (result * 59) + ($startTimeStr == null ? 43 : $startTimeStr.hashCode());
        Object $endTimeStr = getEndTimeStr();
        int result3 = (result2 * 59) + ($endTimeStr == null ? 43 : $endTimeStr.hashCode());
        Object $accountNameLike = getAccountNameLike();
        return (result3 * 59) + ($accountNameLike == null ? 43 : $accountNameLike.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "OperLogListRequest(paginator=" + getPaginator() + ", startTimeStr=" + getStartTimeStr() + ", endTimeStr=" + getEndTimeStr() + ", accountNameLike=" + getAccountNameLike() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public String getStartTimeStr() {
        return this.startTimeStr;
    }

    public String getEndTimeStr() {
        return this.endTimeStr;
    }

    public String getAccountNameLike() {
        return this.accountNameLike;
    }
}
