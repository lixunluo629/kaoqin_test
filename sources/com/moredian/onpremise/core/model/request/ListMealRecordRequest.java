package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "就餐记录列表查询请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ListMealRecordRequest.class */
public class ListMealRecordRequest extends BaseRequest {
    private static final long serialVersionUID = 4358384115705217226L;

    @ApiModelProperty(name = "paginator", value = "分页请求参数")
    private Paginator paginator;

    @ApiModelProperty(name = "startTimeStr", value = "开始时间，格式：yyyy-MM-dd 或 yyyy-MM-dd hh:mm:ss")
    private String startTimeStr;

    @ApiModelProperty(name = "endTimeStr", value = "结束时间，格式：yyyy-MM-dd 或 yyyy-MM-dd hh:mm:ss")
    private String endTimeStr;

    @ApiModelProperty(name = "canteenId", value = "餐厅id")
    private Long canteenId;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "deptIdList", value = "部门id列表", hidden = true)
    private List<Long> deptIdList;

    @ApiModelProperty(name = "manageDeptIdList", value = "管理部门id", hidden = true)
    private List<String> manageDeptIdList;

    @ApiModelProperty(name = "name", value = "用户姓名或工号")
    private String name;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "manageDeviceSnList", value = "账户管理sn列表", hidden = true)
    private List<String> manageDeviceSnList;

    @ApiModelProperty(name = "verifyResult", value = "识别结果", hidden = true)
    private Integer verifyResult;

    @ApiModelProperty(name = "verifyResultList", value = "识别结果列表", hidden = true)
    private List<Integer> verifyResultList;

    @ApiModelProperty(name = "canteenRegion", value = "餐厅区域")
    private String canteenRegion;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public void setCanteenId(Long canteenId) {
        this.canteenId = canteenId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setDeptIdList(List<Long> deptIdList) {
        this.deptIdList = deptIdList;
    }

    public void setManageDeptIdList(List<String> manageDeptIdList) {
        this.manageDeptIdList = manageDeptIdList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setManageDeviceSnList(List<String> manageDeviceSnList) {
        this.manageDeviceSnList = manageDeviceSnList;
    }

    public void setVerifyResult(Integer verifyResult) {
        this.verifyResult = verifyResult;
    }

    public void setVerifyResultList(List<Integer> verifyResultList) {
        this.verifyResultList = verifyResultList;
    }

    public void setCanteenRegion(String canteenRegion) {
        this.canteenRegion = canteenRegion;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListMealRecordRequest)) {
            return false;
        }
        ListMealRecordRequest other = (ListMealRecordRequest) o;
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
        Object this$canteenId = getCanteenId();
        Object other$canteenId = other.getCanteenId();
        if (this$canteenId == null) {
            if (other$canteenId != null) {
                return false;
            }
        } else if (!this$canteenId.equals(other$canteenId)) {
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
        Object this$deptIdList = getDeptIdList();
        Object other$deptIdList = other.getDeptIdList();
        if (this$deptIdList == null) {
            if (other$deptIdList != null) {
                return false;
            }
        } else if (!this$deptIdList.equals(other$deptIdList)) {
            return false;
        }
        Object this$manageDeptIdList = getManageDeptIdList();
        Object other$manageDeptIdList = other.getManageDeptIdList();
        if (this$manageDeptIdList == null) {
            if (other$manageDeptIdList != null) {
                return false;
            }
        } else if (!this$manageDeptIdList.equals(other$manageDeptIdList)) {
            return false;
        }
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name == null) {
            if (other$name != null) {
                return false;
            }
        } else if (!this$name.equals(other$name)) {
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
        Object this$manageDeviceSnList = getManageDeviceSnList();
        Object other$manageDeviceSnList = other.getManageDeviceSnList();
        if (this$manageDeviceSnList == null) {
            if (other$manageDeviceSnList != null) {
                return false;
            }
        } else if (!this$manageDeviceSnList.equals(other$manageDeviceSnList)) {
            return false;
        }
        Object this$verifyResult = getVerifyResult();
        Object other$verifyResult = other.getVerifyResult();
        if (this$verifyResult == null) {
            if (other$verifyResult != null) {
                return false;
            }
        } else if (!this$verifyResult.equals(other$verifyResult)) {
            return false;
        }
        Object this$verifyResultList = getVerifyResultList();
        Object other$verifyResultList = other.getVerifyResultList();
        if (this$verifyResultList == null) {
            if (other$verifyResultList != null) {
                return false;
            }
        } else if (!this$verifyResultList.equals(other$verifyResultList)) {
            return false;
        }
        Object this$canteenRegion = getCanteenRegion();
        Object other$canteenRegion = other.getCanteenRegion();
        return this$canteenRegion == null ? other$canteenRegion == null : this$canteenRegion.equals(other$canteenRegion);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ListMealRecordRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $startTimeStr = getStartTimeStr();
        int result2 = (result * 59) + ($startTimeStr == null ? 43 : $startTimeStr.hashCode());
        Object $endTimeStr = getEndTimeStr();
        int result3 = (result2 * 59) + ($endTimeStr == null ? 43 : $endTimeStr.hashCode());
        Object $canteenId = getCanteenId();
        int result4 = (result3 * 59) + ($canteenId == null ? 43 : $canteenId.hashCode());
        Object $deptId = getDeptId();
        int result5 = (result4 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deptIdList = getDeptIdList();
        int result6 = (result5 * 59) + ($deptIdList == null ? 43 : $deptIdList.hashCode());
        Object $manageDeptIdList = getManageDeptIdList();
        int result7 = (result6 * 59) + ($manageDeptIdList == null ? 43 : $manageDeptIdList.hashCode());
        Object $name = getName();
        int result8 = (result7 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $deviceSn = getDeviceSn();
        int result9 = (result8 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $manageDeviceSnList = getManageDeviceSnList();
        int result10 = (result9 * 59) + ($manageDeviceSnList == null ? 43 : $manageDeviceSnList.hashCode());
        Object $verifyResult = getVerifyResult();
        int result11 = (result10 * 59) + ($verifyResult == null ? 43 : $verifyResult.hashCode());
        Object $verifyResultList = getVerifyResultList();
        int result12 = (result11 * 59) + ($verifyResultList == null ? 43 : $verifyResultList.hashCode());
        Object $canteenRegion = getCanteenRegion();
        return (result12 * 59) + ($canteenRegion == null ? 43 : $canteenRegion.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ListMealRecordRequest(paginator=" + getPaginator() + ", startTimeStr=" + getStartTimeStr() + ", endTimeStr=" + getEndTimeStr() + ", canteenId=" + getCanteenId() + ", deptId=" + getDeptId() + ", deptIdList=" + getDeptIdList() + ", manageDeptIdList=" + getManageDeptIdList() + ", name=" + getName() + ", deviceSn=" + getDeviceSn() + ", manageDeviceSnList=" + getManageDeviceSnList() + ", verifyResult=" + getVerifyResult() + ", verifyResultList=" + getVerifyResultList() + ", canteenRegion=" + getCanteenRegion() + ")";
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

    public Long getCanteenId() {
        return this.canteenId;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public List<Long> getDeptIdList() {
        return this.deptIdList;
    }

    public List<String> getManageDeptIdList() {
        return this.manageDeptIdList;
    }

    public String getName() {
        return this.name;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public List<String> getManageDeviceSnList() {
        return this.manageDeviceSnList;
    }

    public Integer getVerifyResult() {
        return this.verifyResult;
    }

    public List<Integer> getVerifyResultList() {
        return this.verifyResultList;
    }

    public String getCanteenRegion() {
        return this.canteenRegion;
    }
}
