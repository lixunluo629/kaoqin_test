package com.moredian.onpremise.core.model.request;

import com.alibaba.excel.constant.ExcelXmlConstants;
import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "统计就餐记录请求信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/CountMealRecordRequest.class */
public class CountMealRecordRequest extends BaseRequest {
    private static final long serialVersionUID = 5274973768879241049L;

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(name = "startTimeStr", value = "开始时间，格式：yyyy-MM-dd 或者 yyyy-MM-dd hh:mm:ss")
    private String startTimeStr;

    @ApiModelProperty(name = "endTimeStr", value = "结束时间，格式：yyyy-MM-dd 或者 yyyy-MM-dd hh:mm:ss")
    private String endTimeStr;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "mealCanteenId", value = "餐厅id")
    private Long mealCanteenId;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "manageDeptList", value = "当前管理员管理部门列表", hidden = true)
    private List<String> manageDeptList;

    @ApiModelProperty(name = "manageDeviceList", value = "当前管理员管理设备列表", hidden = true)
    private List<String> manageDeviceList;

    @ApiModelProperty(name = "type", value = "1-每日消费统计；2-人员消费统计")
    private Integer type;

    @ApiModelProperty(name = ExcelXmlConstants.DIMENSION, value = "统计维度：1-部门；2-人员")
    private Integer dimension;

    @ApiModelProperty(name = "canteenRegion", value = "餐厅区域")
    private String canteenRegion;

    @ApiModelProperty(name = "manageAccountId", value = "创建者账户id")
    private Integer manageAccountId;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setMealCanteenId(Long mealCanteenId) {
        this.mealCanteenId = mealCanteenId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setManageDeptList(List<String> manageDeptList) {
        this.manageDeptList = manageDeptList;
    }

    public void setManageDeviceList(List<String> manageDeviceList) {
        this.manageDeviceList = manageDeviceList;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setDimension(Integer dimension) {
        this.dimension = dimension;
    }

    public void setCanteenRegion(String canteenRegion) {
        this.canteenRegion = canteenRegion;
    }

    public void setManageAccountId(Integer manageAccountId) {
        this.manageAccountId = manageAccountId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CountMealRecordRequest)) {
            return false;
        }
        CountMealRecordRequest other = (CountMealRecordRequest) o;
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
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        if (this$deviceSn == null) {
            if (other$deviceSn != null) {
                return false;
            }
        } else if (!this$deviceSn.equals(other$deviceSn)) {
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
        Object this$deptId = getDeptId();
        Object other$deptId = other.getDeptId();
        if (this$deptId == null) {
            if (other$deptId != null) {
                return false;
            }
        } else if (!this$deptId.equals(other$deptId)) {
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
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type == null) {
            if (other$type != null) {
                return false;
            }
        } else if (!this$type.equals(other$type)) {
            return false;
        }
        Object this$dimension = getDimension();
        Object other$dimension = other.getDimension();
        if (this$dimension == null) {
            if (other$dimension != null) {
                return false;
            }
        } else if (!this$dimension.equals(other$dimension)) {
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
        Object this$manageAccountId = getManageAccountId();
        Object other$manageAccountId = other.getManageAccountId();
        return this$manageAccountId == null ? other$manageAccountId == null : this$manageAccountId.equals(other$manageAccountId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof CountMealRecordRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $startTimeStr = getStartTimeStr();
        int result2 = (result * 59) + ($startTimeStr == null ? 43 : $startTimeStr.hashCode());
        Object $endTimeStr = getEndTimeStr();
        int result3 = (result2 * 59) + ($endTimeStr == null ? 43 : $endTimeStr.hashCode());
        Object $deviceSn = getDeviceSn();
        int result4 = (result3 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $mealCanteenId = getMealCanteenId();
        int result5 = (result4 * 59) + ($mealCanteenId == null ? 43 : $mealCanteenId.hashCode());
        Object $deptId = getDeptId();
        int result6 = (result5 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $manageDeptList = getManageDeptList();
        int result7 = (result6 * 59) + ($manageDeptList == null ? 43 : $manageDeptList.hashCode());
        Object $manageDeviceList = getManageDeviceList();
        int result8 = (result7 * 59) + ($manageDeviceList == null ? 43 : $manageDeviceList.hashCode());
        Object $type = getType();
        int result9 = (result8 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $dimension = getDimension();
        int result10 = (result9 * 59) + ($dimension == null ? 43 : $dimension.hashCode());
        Object $canteenRegion = getCanteenRegion();
        int result11 = (result10 * 59) + ($canteenRegion == null ? 43 : $canteenRegion.hashCode());
        Object $manageAccountId = getManageAccountId();
        return (result11 * 59) + ($manageAccountId == null ? 43 : $manageAccountId.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "CountMealRecordRequest(paginator=" + getPaginator() + ", startTimeStr=" + getStartTimeStr() + ", endTimeStr=" + getEndTimeStr() + ", deviceSn=" + getDeviceSn() + ", mealCanteenId=" + getMealCanteenId() + ", deptId=" + getDeptId() + ", manageDeptList=" + getManageDeptList() + ", manageDeviceList=" + getManageDeviceList() + ", type=" + getType() + ", dimension=" + getDimension() + ", canteenRegion=" + getCanteenRegion() + ", manageAccountId=" + getManageAccountId() + ")";
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

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Long getMealCanteenId() {
        return this.mealCanteenId;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public List<String> getManageDeptList() {
        return this.manageDeptList;
    }

    public List<String> getManageDeviceList() {
        return this.manageDeviceList;
    }

    public Integer getType() {
        return this.type;
    }

    public Integer getDimension() {
        return this.dimension;
    }

    public String getCanteenRegion() {
        return this.canteenRegion;
    }

    public Integer getManageAccountId() {
        return this.manageAccountId;
    }
}
