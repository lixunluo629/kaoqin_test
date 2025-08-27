package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "获取餐厅列表请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ListCanteenRequest.class */
public class ListCanteenRequest extends BaseRequest {
    private static final long serialVersionUID = -9216619979622397749L;

    @ApiModelProperty(name = "paginator", value = "分页查询对象")
    private Paginator paginator;

    @ApiModelProperty(name = "canteenName", value = "餐厅名称，模糊查询")
    private String canteenName;

    @ApiModelProperty(name = "canteenAddress", value = "餐厅地址，模糊查询")
    private String canteenAddress;

    @ApiModelProperty(name = "canteenRegion", value = "餐厅区域")
    private String canteenRegion;

    @ApiModelProperty(name = "canteenOwnerName", value = "餐厅负责人姓名，模糊查询")
    private String canteenOwnerName;

    @ApiModelProperty(name = "canteenOwnerMobile", value = "餐厅负责人手机号，模糊查询")
    private String canteenOwnerMobile;

    @ApiModelProperty(name = "manageAccountId", value = "创建者账户id")
    private Integer manageAccountId;

    @ApiModelProperty(name = "disableCount", value = "取消统计项，0-不取消，统计（默认）；1-取消、不统计")
    private Integer disableCount;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }

    public void setCanteenAddress(String canteenAddress) {
        this.canteenAddress = canteenAddress;
    }

    public void setCanteenRegion(String canteenRegion) {
        this.canteenRegion = canteenRegion;
    }

    public void setCanteenOwnerName(String canteenOwnerName) {
        this.canteenOwnerName = canteenOwnerName;
    }

    public void setCanteenOwnerMobile(String canteenOwnerMobile) {
        this.canteenOwnerMobile = canteenOwnerMobile;
    }

    public void setManageAccountId(Integer manageAccountId) {
        this.manageAccountId = manageAccountId;
    }

    public void setDisableCount(Integer disableCount) {
        this.disableCount = disableCount;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListCanteenRequest)) {
            return false;
        }
        ListCanteenRequest other = (ListCanteenRequest) o;
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
        Object this$canteenName = getCanteenName();
        Object other$canteenName = other.getCanteenName();
        if (this$canteenName == null) {
            if (other$canteenName != null) {
                return false;
            }
        } else if (!this$canteenName.equals(other$canteenName)) {
            return false;
        }
        Object this$canteenAddress = getCanteenAddress();
        Object other$canteenAddress = other.getCanteenAddress();
        if (this$canteenAddress == null) {
            if (other$canteenAddress != null) {
                return false;
            }
        } else if (!this$canteenAddress.equals(other$canteenAddress)) {
            return false;
        }
        Object this$canteenRegion = getCanteenRegion();
        Object other$canteenRegion = other.getCanteenRegion();
        if (this$canteenRegion == null) {
            if (other$canteenRegion != null) {
                return false;
            }
        } else if (!this$canteenRegion.equals(other$canteenRegion)) {
            return false;
        }
        Object this$canteenOwnerName = getCanteenOwnerName();
        Object other$canteenOwnerName = other.getCanteenOwnerName();
        if (this$canteenOwnerName == null) {
            if (other$canteenOwnerName != null) {
                return false;
            }
        } else if (!this$canteenOwnerName.equals(other$canteenOwnerName)) {
            return false;
        }
        Object this$canteenOwnerMobile = getCanteenOwnerMobile();
        Object other$canteenOwnerMobile = other.getCanteenOwnerMobile();
        if (this$canteenOwnerMobile == null) {
            if (other$canteenOwnerMobile != null) {
                return false;
            }
        } else if (!this$canteenOwnerMobile.equals(other$canteenOwnerMobile)) {
            return false;
        }
        Object this$manageAccountId = getManageAccountId();
        Object other$manageAccountId = other.getManageAccountId();
        if (this$manageAccountId == null) {
            if (other$manageAccountId != null) {
                return false;
            }
        } else if (!this$manageAccountId.equals(other$manageAccountId)) {
            return false;
        }
        Object this$disableCount = getDisableCount();
        Object other$disableCount = other.getDisableCount();
        return this$disableCount == null ? other$disableCount == null : this$disableCount.equals(other$disableCount);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ListCanteenRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $canteenName = getCanteenName();
        int result2 = (result * 59) + ($canteenName == null ? 43 : $canteenName.hashCode());
        Object $canteenAddress = getCanteenAddress();
        int result3 = (result2 * 59) + ($canteenAddress == null ? 43 : $canteenAddress.hashCode());
        Object $canteenRegion = getCanteenRegion();
        int result4 = (result3 * 59) + ($canteenRegion == null ? 43 : $canteenRegion.hashCode());
        Object $canteenOwnerName = getCanteenOwnerName();
        int result5 = (result4 * 59) + ($canteenOwnerName == null ? 43 : $canteenOwnerName.hashCode());
        Object $canteenOwnerMobile = getCanteenOwnerMobile();
        int result6 = (result5 * 59) + ($canteenOwnerMobile == null ? 43 : $canteenOwnerMobile.hashCode());
        Object $manageAccountId = getManageAccountId();
        int result7 = (result6 * 59) + ($manageAccountId == null ? 43 : $manageAccountId.hashCode());
        Object $disableCount = getDisableCount();
        return (result7 * 59) + ($disableCount == null ? 43 : $disableCount.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ListCanteenRequest(paginator=" + getPaginator() + ", canteenName=" + getCanteenName() + ", canteenAddress=" + getCanteenAddress() + ", canteenRegion=" + getCanteenRegion() + ", canteenOwnerName=" + getCanteenOwnerName() + ", canteenOwnerMobile=" + getCanteenOwnerMobile() + ", manageAccountId=" + getManageAccountId() + ", disableCount=" + getDisableCount() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public String getCanteenName() {
        return this.canteenName;
    }

    public String getCanteenAddress() {
        return this.canteenAddress;
    }

    public String getCanteenRegion() {
        return this.canteenRegion;
    }

    public String getCanteenOwnerName() {
        return this.canteenOwnerName;
    }

    public String getCanteenOwnerMobile() {
        return this.canteenOwnerMobile;
    }

    public Integer getManageAccountId() {
        return this.manageAccountId;
    }

    public Integer getDisableCount() {
        return this.disableCount;
    }
}
