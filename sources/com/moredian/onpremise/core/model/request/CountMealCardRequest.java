package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "统计团餐饭卡记录请求信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/CountMealCardRequest.class */
public class CountMealCardRequest extends BaseRequest {
    private static final long serialVersionUID = -8236748474314176546L;

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(name = "startTimeStr", value = "开始时间，格式：yyyy-MM-dd 或者 yyyy-MM-dd hh:mm:ss")
    private String startTimeStr;

    @ApiModelProperty(name = "endTimeStr", value = "结束时间，格式：yyyy-MM-dd 或者 yyyy-MM-dd hh:mm:ss")
    private String endTimeStr;

    @ApiModelProperty(name = "mealCanteenId", value = "餐厅id")
    private Long mealCanteenId;

    @ApiModelProperty(name = "manageDeptList", value = "当前管理员管理部门列表", hidden = true)
    private List<String> manageDeptList;

    @ApiModelProperty(name = "manageDeviceList", value = "当前管理员管理设备列表", hidden = true)
    private List<String> manageDeviceList;

    @ApiModelProperty(name = "canteenRegion", value = "餐厅区域")
    private String canteenRegion;

    @ApiModelProperty(name = "name", value = "用户姓名或工号")
    private String name;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public void setMealCanteenId(Long mealCanteenId) {
        this.mealCanteenId = mealCanteenId;
    }

    public void setManageDeptList(List<String> manageDeptList) {
        this.manageDeptList = manageDeptList;
    }

    public void setManageDeviceList(List<String> manageDeviceList) {
        this.manageDeviceList = manageDeviceList;
    }

    public void setCanteenRegion(String canteenRegion) {
        this.canteenRegion = canteenRegion;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CountMealCardRequest)) {
            return false;
        }
        CountMealCardRequest other = (CountMealCardRequest) o;
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
        Object this$startTimeStr = getStartTimeStr();
        Object other$startTimeStr = other.getStartTimeStr();
        if (this$startTimeStr == null) {
            if (other$startTimeStr != null) {
                return false;
            }
        } else if (!this$startTimeStr.equals(other$startTimeStr)) {
            return false;
        }
        Object this$endTimeStr = getEndTimeStr();
        Object other$endTimeStr = other.getEndTimeStr();
        if (this$endTimeStr == null) {
            if (other$endTimeStr != null) {
                return false;
            }
        } else if (!this$endTimeStr.equals(other$endTimeStr)) {
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
        Object this$manageDeptList = getManageDeptList();
        Object other$manageDeptList = other.getManageDeptList();
        if (this$manageDeptList == null) {
            if (other$manageDeptList != null) {
                return false;
            }
        } else if (!this$manageDeptList.equals(other$manageDeptList)) {
            return false;
        }
        Object this$manageDeviceList = getManageDeviceList();
        Object other$manageDeviceList = other.getManageDeviceList();
        if (this$manageDeviceList == null) {
            if (other$manageDeviceList != null) {
                return false;
            }
        } else if (!this$manageDeviceList.equals(other$manageDeviceList)) {
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
        Object this$name = getName();
        Object other$name = other.getName();
        return this$name == null ? other$name == null : this$name.equals(other$name);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof CountMealCardRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $startTimeStr = getStartTimeStr();
        int result2 = (result * 59) + ($startTimeStr == null ? 43 : $startTimeStr.hashCode());
        Object $endTimeStr = getEndTimeStr();
        int result3 = (result2 * 59) + ($endTimeStr == null ? 43 : $endTimeStr.hashCode());
        Object $mealCanteenId = getMealCanteenId();
        int result4 = (result3 * 59) + ($mealCanteenId == null ? 43 : $mealCanteenId.hashCode());
        Object $manageDeptList = getManageDeptList();
        int result5 = (result4 * 59) + ($manageDeptList == null ? 43 : $manageDeptList.hashCode());
        Object $manageDeviceList = getManageDeviceList();
        int result6 = (result5 * 59) + ($manageDeviceList == null ? 43 : $manageDeviceList.hashCode());
        Object $canteenRegion = getCanteenRegion();
        int result7 = (result6 * 59) + ($canteenRegion == null ? 43 : $canteenRegion.hashCode());
        Object $name = getName();
        return (result7 * 59) + ($name == null ? 43 : $name.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "CountMealCardRequest(paginator=" + getPaginator() + ", startTimeStr=" + getStartTimeStr() + ", endTimeStr=" + getEndTimeStr() + ", mealCanteenId=" + getMealCanteenId() + ", manageDeptList=" + getManageDeptList() + ", manageDeviceList=" + getManageDeviceList() + ", canteenRegion=" + getCanteenRegion() + ", name=" + getName() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public String getStartTimeStr() {
        return this.startTimeStr;
    }

    public String getEndTimeStr() {
        return this.endTimeStr;
    }

    public Long getMealCanteenId() {
        return this.mealCanteenId;
    }

    public List<String> getManageDeptList() {
        return this.manageDeptList;
    }

    public List<String> getManageDeviceList() {
        return this.manageDeviceList;
    }

    public String getCanteenRegion() {
        return this.canteenRegion;
    }

    public String getName() {
        return this.name;
    }
}
