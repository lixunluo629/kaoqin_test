package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "管理员列表请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ListAccountRequest.class */
public class ListAccountRequest extends BaseRequest {

    @ApiModelProperty(name = "paginator", value = "分页对象")
    private Paginator paginator;

    @ApiModelProperty(name = "searchKey", value = "搜索名称")
    private String searchKey;

    @ApiModelProperty(name = "accountGrade", value = "管理员级别")
    private Integer accountGrade;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public void setAccountGrade(Integer accountGrade) {
        this.accountGrade = accountGrade;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListAccountRequest)) {
            return false;
        }
        ListAccountRequest other = (ListAccountRequest) o;
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
        Object this$searchKey = getSearchKey();
        Object other$searchKey = other.getSearchKey();
        if (this$searchKey == null) {
            if (other$searchKey != null) {
                return false;
            }
        } else if (!this$searchKey.equals(other$searchKey)) {
            return false;
        }
        Object this$accountGrade = getAccountGrade();
        Object other$accountGrade = other.getAccountGrade();
        return this$accountGrade == null ? other$accountGrade == null : this$accountGrade.equals(other$accountGrade);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ListAccountRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $searchKey = getSearchKey();
        int result2 = (result * 59) + ($searchKey == null ? 43 : $searchKey.hashCode());
        Object $accountGrade = getAccountGrade();
        return (result2 * 59) + ($accountGrade == null ? 43 : $accountGrade.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ListAccountRequest(paginator=" + getPaginator() + ", searchKey=" + getSearchKey() + ", accountGrade=" + getAccountGrade() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public String getSearchKey() {
        return this.searchKey;
    }

    public Integer getAccountGrade() {
        return this.accountGrade;
    }
}
