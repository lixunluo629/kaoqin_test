package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "删除餐厅请求信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeleteMealCanteenRequest.class */
public class DeleteMealCanteenRequest extends BaseRequest {
    private static final long serialVersionUID = -7354006412251914831L;

    @ApiModelProperty(name = "mealCanteenId", value = "餐厅id")
    private Long mealCanteenId;

    @ApiModelProperty(name = "canteenName", value = "餐厅名称，此处为餐厅全名称精确搜索，不支持模糊匹配")
    private String canteenName;

    public void setMealCanteenId(Long mealCanteenId) {
        this.mealCanteenId = mealCanteenId;
    }

    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeleteMealCanteenRequest)) {
            return false;
        }
        DeleteMealCanteenRequest other = (DeleteMealCanteenRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$mealCanteenId = getMealCanteenId();
        Object other$mealCanteenId = other.getMealCanteenId();
        if (this$mealCanteenId == null) {
            if (other$mealCanteenId != null) {
                return false;
            }
        } else if (!this$mealCanteenId.equals(other$mealCanteenId)) {
            return false;
        }
        Object this$canteenName = getCanteenName();
        Object other$canteenName = other.getCanteenName();
        return this$canteenName == null ? other$canteenName == null : this$canteenName.equals(other$canteenName);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DeleteMealCanteenRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $mealCanteenId = getMealCanteenId();
        int result = (1 * 59) + ($mealCanteenId == null ? 43 : $mealCanteenId.hashCode());
        Object $canteenName = getCanteenName();
        return (result * 59) + ($canteenName == null ? 43 : $canteenName.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DeleteMealCanteenRequest(mealCanteenId=" + getMealCanteenId() + ", canteenName=" + getCanteenName() + ")";
    }

    public Long getMealCanteenId() {
        return this.mealCanteenId;
    }

    public String getCanteenName() {
        return this.canteenName;
    }
}
