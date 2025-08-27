package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.model.dto.CanteenTimeDto;
import com.moredian.onpremise.core.model.dto.DeviceDto;
import com.moredian.onpremise.core.model.dto.GroupMemberDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "餐厅详细信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/QueryCanteenDetailsResponse.class */
public class QueryCanteenDetailsResponse implements Serializable {
    private static final long serialVersionUID = 7053406710070788115L;

    @ApiModelProperty(name = "mealCanteenId", value = "餐厅id")
    private Long mealCanteenId;

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

    @ApiModelProperty(name = "canteenDevices", value = "餐厅消费机信息")
    private List<DeviceDto> canteenDevices;

    @ApiModelProperty(name = "canteenMembers", value = "餐厅就餐人员")
    private List<GroupMemberDto> canteenMembers;

    @ApiModelProperty(name = "canteenTimes", value = "餐厅就餐时间")
    private List<CanteenTimeDto> canteenTimes;

    public void setMealCanteenId(Long mealCanteenId) {
        this.mealCanteenId = mealCanteenId;
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

    public void setCanteenDevices(List<DeviceDto> canteenDevices) {
        this.canteenDevices = canteenDevices;
    }

    public void setCanteenMembers(List<GroupMemberDto> canteenMembers) {
        this.canteenMembers = canteenMembers;
    }

    public void setCanteenTimes(List<CanteenTimeDto> canteenTimes) {
        this.canteenTimes = canteenTimes;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryCanteenDetailsResponse)) {
            return false;
        }
        QueryCanteenDetailsResponse other = (QueryCanteenDetailsResponse) o;
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
        Object this$canteenDevices = getCanteenDevices();
        Object other$canteenDevices = other.getCanteenDevices();
        if (this$canteenDevices == null) {
            if (other$canteenDevices != null) {
                return false;
            }
        } else if (!this$canteenDevices.equals(other$canteenDevices)) {
            return false;
        }
        Object this$canteenMembers = getCanteenMembers();
        Object other$canteenMembers = other.getCanteenMembers();
        if (this$canteenMembers == null) {
            if (other$canteenMembers != null) {
                return false;
            }
        } else if (!this$canteenMembers.equals(other$canteenMembers)) {
            return false;
        }
        Object this$canteenTimes = getCanteenTimes();
        Object other$canteenTimes = other.getCanteenTimes();
        return this$canteenTimes == null ? other$canteenTimes == null : this$canteenTimes.equals(other$canteenTimes);
    }

    protected boolean canEqual(Object other) {
        return other instanceof QueryCanteenDetailsResponse;
    }

    public int hashCode() {
        Object $mealCanteenId = getMealCanteenId();
        int result = (1 * 59) + ($mealCanteenId == null ? 43 : $mealCanteenId.hashCode());
        Object $canteenName = getCanteenName();
        int result2 = (result * 59) + ($canteenName == null ? 43 : $canteenName.hashCode());
        Object $canteenScope = getCanteenScope();
        int result3 = (result2 * 59) + ($canteenScope == null ? 43 : $canteenScope.hashCode());
        Object $canteenOwnerName = getCanteenOwnerName();
        int result4 = (result3 * 59) + ($canteenOwnerName == null ? 43 : $canteenOwnerName.hashCode());
        Object $canteenOwnerMobile = getCanteenOwnerMobile();
        int result5 = (result4 * 59) + ($canteenOwnerMobile == null ? 43 : $canteenOwnerMobile.hashCode());
        Object $canteenAddress = getCanteenAddress();
        int result6 = (result5 * 59) + ($canteenAddress == null ? 43 : $canteenAddress.hashCode());
        Object $canteenRegion = getCanteenRegion();
        int result7 = (result6 * 59) + ($canteenRegion == null ? 43 : $canteenRegion.hashCode());
        Object $canteenDevices = getCanteenDevices();
        int result8 = (result7 * 59) + ($canteenDevices == null ? 43 : $canteenDevices.hashCode());
        Object $canteenMembers = getCanteenMembers();
        int result9 = (result8 * 59) + ($canteenMembers == null ? 43 : $canteenMembers.hashCode());
        Object $canteenTimes = getCanteenTimes();
        return (result9 * 59) + ($canteenTimes == null ? 43 : $canteenTimes.hashCode());
    }

    public String toString() {
        return "QueryCanteenDetailsResponse(mealCanteenId=" + getMealCanteenId() + ", canteenName=" + getCanteenName() + ", canteenScope=" + getCanteenScope() + ", canteenOwnerName=" + getCanteenOwnerName() + ", canteenOwnerMobile=" + getCanteenOwnerMobile() + ", canteenAddress=" + getCanteenAddress() + ", canteenRegion=" + getCanteenRegion() + ", canteenDevices=" + getCanteenDevices() + ", canteenMembers=" + getCanteenMembers() + ", canteenTimes=" + getCanteenTimes() + ")";
    }

    public Long getMealCanteenId() {
        return this.mealCanteenId;
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

    public List<DeviceDto> getCanteenDevices() {
        return this.canteenDevices;
    }

    public List<GroupMemberDto> getCanteenMembers() {
        return this.canteenMembers;
    }

    public List<CanteenTimeDto> getCanteenTimes() {
        return this.canteenTimes;
    }
}
