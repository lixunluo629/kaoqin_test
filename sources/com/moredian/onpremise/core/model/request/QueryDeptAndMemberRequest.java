package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "模糊搜索部门或成员")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/QueryDeptAndMemberRequest.class */
public class QueryDeptAndMemberRequest extends BaseRequest {

    @ApiModelProperty(name = "searchKey", value = "搜索名称")
    private String searchKey;

    @ApiModelProperty(name = "type", value = "搜索类型：1-部门，2-成员，不传表示全类型查询")
    private Integer type;

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryDeptAndMemberRequest)) {
            return false;
        }
        QueryDeptAndMemberRequest other = (QueryDeptAndMemberRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$type = getType();
        Object other$type = other.getType();
        return this$type == null ? other$type == null : this$type.equals(other$type);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof QueryDeptAndMemberRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $searchKey = getSearchKey();
        int result = (1 * 59) + ($searchKey == null ? 43 : $searchKey.hashCode());
        Object $type = getType();
        return (result * 59) + ($type == null ? 43 : $type.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "QueryDeptAndMemberRequest(searchKey=" + getSearchKey() + ", type=" + getType() + ")";
    }

    public String getSearchKey() {
        return this.searchKey;
    }

    public Integer getType() {
        return this.type;
    }
}
