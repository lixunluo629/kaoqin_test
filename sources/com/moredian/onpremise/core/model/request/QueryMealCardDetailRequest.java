package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "查询团餐卡详细信息请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/QueryMealCardDetailRequest.class */
public class QueryMealCardDetailRequest extends BaseRequest {
    private static final long serialVersionUID = -7295468797563017857L;

    @ApiModelProperty(name = "mealCardId", value = "卡id，新增时不传，修改时必填")
    private Long mealCardId;

    public void setMealCardId(Long mealCardId) {
        this.mealCardId = mealCardId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryMealCardDetailRequest)) {
            return false;
        }
        QueryMealCardDetailRequest other = (QueryMealCardDetailRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$mealCardId = getMealCardId();
        Object other$mealCardId = other.getMealCardId();
        return this$mealCardId == null ? other$mealCardId == null : this$mealCardId.equals(other$mealCardId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof QueryMealCardDetailRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $mealCardId = getMealCardId();
        int result = (1 * 59) + ($mealCardId == null ? 43 : $mealCardId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "QueryMealCardDetailRequest(mealCardId=" + getMealCardId() + ")";
    }

    public Long getMealCardId() {
        return this.mealCardId;
    }
}
