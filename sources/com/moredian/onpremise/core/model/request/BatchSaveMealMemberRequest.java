package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "团餐成员批量保存请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/BatchSaveMealMemberRequest.class */
public class BatchSaveMealMemberRequest extends BaseRequest {

    @ApiModelProperty(name = "batchList", value = "批量添加成员json字符串")
    private List<SaveMealMemberRequest> batchList;

    public void setBatchList(List<SaveMealMemberRequest> batchList) {
        this.batchList = batchList;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BatchSaveMealMemberRequest)) {
            return false;
        }
        BatchSaveMealMemberRequest other = (BatchSaveMealMemberRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$batchList = getBatchList();
        Object other$batchList = other.getBatchList();
        return this$batchList == null ? other$batchList == null : this$batchList.equals(other$batchList);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof BatchSaveMealMemberRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $batchList = getBatchList();
        int result = (1 * 59) + ($batchList == null ? 43 : $batchList.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "BatchSaveMealMemberRequest(batchList=" + getBatchList() + ")";
    }

    public List<SaveMealMemberRequest> getBatchList() {
        return this.batchList;
    }
}
