package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "获取餐厅详情请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/QueryCanteenDetailsRequest.class */
public class QueryCanteenDetailsRequest extends BaseRequest {
    private static final long serialVersionUID = -657453425285975383L;

    @ApiModelProperty(name = "mealCanteenId", value = "餐厅id")
    private Long mealCanteenId;

    @ApiModelProperty(name = "canteenName", value = "餐厅名称，此处为餐厅全名称精确搜索，不支持模糊匹配")
    private String canteenName;

    @ApiModelProperty(name = "canteenRegion", value = "餐厅区域")
    private String canteenRegion;

    public void setMealCanteenId(Long mealCanteenId) {
        this.mealCanteenId = mealCanteenId;
    }

    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }

    public void setCanteenRegion(String canteenRegion) {
        this.canteenRegion = canteenRegion;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryCanteenDetailsRequest)) {
            return false;
        }
        QueryCanteenDetailsRequest other = (QueryCanteenDetailsRequest) o;
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
        if (this$canteenName == null) {
            if (other$canteenName != null) {
                return false;
            }
        } else if (!this$canteenName.equals(other$canteenName)) {
            return false;
        }
        Object this$canteenRegion = getCanteenRegion();
        Object other$canteenRegion = other.getCanteenRegion();
        return this$canteenRegion == null ? other$canteenRegion == null : this$canteenRegion.equals(other$canteenRegion);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof QueryCanteenDetailsRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $mealCanteenId = getMealCanteenId();
        int result = (1 * 59) + ($mealCanteenId == null ? 43 : $mealCanteenId.hashCode());
        Object $canteenName = getCanteenName();
        int result2 = (result * 59) + ($canteenName == null ? 43 : $canteenName.hashCode());
        Object $canteenRegion = getCanteenRegion();
        return (result2 * 59) + ($canteenRegion == null ? 43 : $canteenRegion.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "QueryCanteenDetailsRequest(mealCanteenId=" + getMealCanteenId() + ", canteenName=" + getCanteenName() + ", canteenRegion=" + getCanteenRegion() + ")";
    }

    public Long getMealCanteenId() {
        return this.mealCanteenId;
    }

    public String getCanteenName() {
        return this.canteenName;
    }

    public String getCanteenRegion() {
        return this.canteenRegion;
    }
}
