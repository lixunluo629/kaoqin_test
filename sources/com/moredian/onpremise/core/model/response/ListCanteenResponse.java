package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "餐厅列表响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListCanteenResponse.class */
public class ListCanteenResponse implements Serializable {
    private static final long serialVersionUID = 7837082915709218475L;

    @ApiModelProperty(name = "mealCanteenId", value = "餐厅id")
    private Long mealCanteenId;

    @ApiModelProperty(name = "accountId", value = "创建账户id", hidden = true)
    private Long accountId;

    @ApiModelProperty(name = "canteenName", value = "餐厅名称")
    private String canteenName;

    @ApiModelProperty(name = "canteenScope", value = "就餐周期")
    private String canteenScope;

    @ApiModelProperty(name = "canteenOwnerName", value = "餐厅负责人姓名")
    private String canteenOwnerName;

    @ApiModelProperty(name = "canteenOwnerMobile", value = "餐厅负责人手机号")
    private String canteenOwnerMobile;

    @ApiModelProperty(name = "canteenAddress", value = "餐厅地址")
    private String canteenAddress;

    @ApiModelProperty(name = "canteenRegion", value = "餐厅区域")
    private String canteenRegion;

    @ApiModelProperty(name = "accountName", value = "创建人名称")
    private String accountName;

    @ApiModelProperty(name = "memberNum", value = "就餐成员数量")
    private Integer memberNum;

    @ApiModelProperty(name = "deviceNum", value = "就餐设备数量")
    private Integer deviceNum;

    public void setMealCanteenId(Long mealCanteenId) {
        this.mealCanteenId = mealCanteenId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }

    public void setCanteenScope(String canteenScope) {
        this.canteenScope = canteenScope;
    }

    public void setCanteenOwnerName(String canteenOwnerName) {
        this.canteenOwnerName = canteenOwnerName;
    }

    public void setCanteenOwnerMobile(String canteenOwnerMobile) {
        this.canteenOwnerMobile = canteenOwnerMobile;
    }

    public void setCanteenAddress(String canteenAddress) {
        this.canteenAddress = canteenAddress;
    }

    public void setCanteenRegion(String canteenRegion) {
        this.canteenRegion = canteenRegion;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setMemberNum(Integer memberNum) {
        this.memberNum = memberNum;
    }

    public void setDeviceNum(Integer deviceNum) {
        this.deviceNum = deviceNum;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListCanteenResponse)) {
            return false;
        }
        ListCanteenResponse other = (ListCanteenResponse) o;
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
        Object this$accountId = getAccountId();
        Object other$accountId = other.getAccountId();
        if (this$accountId == null) {
            if (other$accountId != null) {
                return false;
            }
        } else if (!this$accountId.equals(other$accountId)) {
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
        Object this$canteenScope = getCanteenScope();
        Object other$canteenScope = other.getCanteenScope();
        if (this$canteenScope == null) {
            if (other$canteenScope != null) {
                return false;
            }
        } else if (!this$canteenScope.equals(other$canteenScope)) {
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
        Object this$accountName = getAccountName();
        Object other$accountName = other.getAccountName();
        if (this$accountName == null) {
            if (other$accountName != null) {
                return false;
            }
        } else if (!this$accountName.equals(other$accountName)) {
            return false;
        }
        Object this$memberNum = getMemberNum();
        Object other$memberNum = other.getMemberNum();
        if (this$memberNum == null) {
            if (other$memberNum != null) {
                return false;
            }
        } else if (!this$memberNum.equals(other$memberNum)) {
            return false;
        }
        Object this$deviceNum = getDeviceNum();
        Object other$deviceNum = other.getDeviceNum();
        return this$deviceNum == null ? other$deviceNum == null : this$deviceNum.equals(other$deviceNum);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListCanteenResponse;
    }

    public int hashCode() {
        Object $mealCanteenId = getMealCanteenId();
        int result = (1 * 59) + ($mealCanteenId == null ? 43 : $mealCanteenId.hashCode());
        Object $accountId = getAccountId();
        int result2 = (result * 59) + ($accountId == null ? 43 : $accountId.hashCode());
        Object $canteenName = getCanteenName();
        int result3 = (result2 * 59) + ($canteenName == null ? 43 : $canteenName.hashCode());
        Object $canteenScope = getCanteenScope();
        int result4 = (result3 * 59) + ($canteenScope == null ? 43 : $canteenScope.hashCode());
        Object $canteenOwnerName = getCanteenOwnerName();
        int result5 = (result4 * 59) + ($canteenOwnerName == null ? 43 : $canteenOwnerName.hashCode());
        Object $canteenOwnerMobile = getCanteenOwnerMobile();
        int result6 = (result5 * 59) + ($canteenOwnerMobile == null ? 43 : $canteenOwnerMobile.hashCode());
        Object $canteenAddress = getCanteenAddress();
        int result7 = (result6 * 59) + ($canteenAddress == null ? 43 : $canteenAddress.hashCode());
        Object $canteenRegion = getCanteenRegion();
        int result8 = (result7 * 59) + ($canteenRegion == null ? 43 : $canteenRegion.hashCode());
        Object $accountName = getAccountName();
        int result9 = (result8 * 59) + ($accountName == null ? 43 : $accountName.hashCode());
        Object $memberNum = getMemberNum();
        int result10 = (result9 * 59) + ($memberNum == null ? 43 : $memberNum.hashCode());
        Object $deviceNum = getDeviceNum();
        return (result10 * 59) + ($deviceNum == null ? 43 : $deviceNum.hashCode());
    }

    public String toString() {
        return "ListCanteenResponse(mealCanteenId=" + getMealCanteenId() + ", accountId=" + getAccountId() + ", canteenName=" + getCanteenName() + ", canteenScope=" + getCanteenScope() + ", canteenOwnerName=" + getCanteenOwnerName() + ", canteenOwnerMobile=" + getCanteenOwnerMobile() + ", canteenAddress=" + getCanteenAddress() + ", canteenRegion=" + getCanteenRegion() + ", accountName=" + getAccountName() + ", memberNum=" + getMemberNum() + ", deviceNum=" + getDeviceNum() + ")";
    }

    public Long getMealCanteenId() {
        return this.mealCanteenId;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public String getCanteenName() {
        return this.canteenName;
    }

    public String getCanteenScope() {
        return this.canteenScope;
    }

    public String getCanteenOwnerName() {
        return this.canteenOwnerName;
    }

    public String getCanteenOwnerMobile() {
        return this.canteenOwnerMobile;
    }

    public String getCanteenAddress() {
        return this.canteenAddress;
    }

    public String getCanteenRegion() {
        return this.canteenRegion;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public Integer getMemberNum() {
        return this.memberNum;
    }

    public Integer getDeviceNum() {
        return this.deviceNum;
    }
}
