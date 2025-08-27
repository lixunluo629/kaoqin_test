package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "团餐卡列表请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ListMealCardRequest.class */
public class ListMealCardRequest extends BaseRequest {
    private static final long serialVersionUID = -6509383371849422063L;

    @ApiModelProperty(name = "paginator", value = "分页请求参数对象")
    private Paginator paginator;

    @ApiModelProperty(name = "cardName", value = "卡名称")
    private String cardName;

    @ApiModelProperty(name = "cardType", value = "卡类型")
    private Integer cardType;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListMealCardRequest)) {
            return false;
        }
        ListMealCardRequest other = (ListMealCardRequest) o;
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
        Object this$cardName = getCardName();
        Object other$cardName = other.getCardName();
        if (this$cardName == null) {
            if (other$cardName != null) {
                return false;
            }
        } else if (!this$cardName.equals(other$cardName)) {
            return false;
        }
        Object this$cardType = getCardType();
        Object other$cardType = other.getCardType();
        return this$cardType == null ? other$cardType == null : this$cardType.equals(other$cardType);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ListMealCardRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $cardName = getCardName();
        int result2 = (result * 59) + ($cardName == null ? 43 : $cardName.hashCode());
        Object $cardType = getCardType();
        return (result2 * 59) + ($cardType == null ? 43 : $cardType.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ListMealCardRequest(paginator=" + getPaginator() + ", cardName=" + getCardName() + ", cardType=" + getCardType() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public String getCardName() {
        return this.cardName;
    }

    public Integer getCardType() {
        return this.cardType;
    }
}
