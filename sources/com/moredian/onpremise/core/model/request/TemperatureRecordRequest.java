package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "测温记录查询请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TemperatureRecordRequest.class */
public class TemperatureRecordRequest extends BaseRequest {
    private static final long serialVersionUID = -2545434942096590905L;

    @ApiModelProperty(name = "paginator", value = "分页请求参数")
    private Paginator paginator;

    @ApiModelProperty(name = "startDatetime", value = "查询开始日期，格式:yyyy-MM-dd HH:mm:ss,例：2019-06-06 09:15:15")
    private String startDatetime;

    @ApiModelProperty(name = "endDatetime", value = "查询结束日期，格式:yyyy-MM-dd HH:mm:ss,例：2019-06-06 23:15:15")
    private String endDatetime;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "temperatureStatus", value = "温度状态：1-正常，2-高温")
    private Integer temperatureStatus;

    @ApiModelProperty(name = "deptIdList", value = "部门id列表", hidden = true)
    private List<Long> deptIdList;

    @ApiModelProperty(name = "manageDeptIdList", value = "管理部门id", hidden = true)
    private List<String> manageDeptIdList;

    @ApiModelProperty(name = "manageDeviceSnList", value = "账户管理sn列表", hidden = true)
    private List<String> manageDeviceSnList;

    @ApiModelProperty(name = "keywords", value = "成员名称或工号")
    private String keywords;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setTemperatureStatus(Integer temperatureStatus) {
        this.temperatureStatus = temperatureStatus;
    }

    public void setDeptIdList(List<Long> deptIdList) {
        this.deptIdList = deptIdList;
    }

    public void setManageDeptIdList(List<String> manageDeptIdList) {
        this.manageDeptIdList = manageDeptIdList;
    }

    public void setManageDeviceSnList(List<String> manageDeviceSnList) {
        this.manageDeviceSnList = manageDeviceSnList;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TemperatureRecordRequest)) {
            return false;
        }
        TemperatureRecordRequest other = (TemperatureRecordRequest) o;
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
        Object this$startDatetime = getStartDatetime();
        Object other$startDatetime = other.getStartDatetime();
        if (this$startDatetime == null) {
            if (other$startDatetime != null) {
                return false;
            }
        } else if (!this$startDatetime.equals(other$startDatetime)) {
            return false;
        }
        Object this$endDatetime = getEndDatetime();
        Object other$endDatetime = other.getEndDatetime();
        if (this$endDatetime == null) {
            if (other$endDatetime != null) {
                return false;
            }
        } else if (!this$endDatetime.equals(other$endDatetime)) {
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
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        if (this$deviceSn == null) {
            if (other$deviceSn != null) {
                return false;
            }
        } else if (!this$deviceSn.equals(other$deviceSn)) {
            return false;
        }
        Object this$temperatureStatus = getTemperatureStatus();
        Object other$temperatureStatus = other.getTemperatureStatus();
        if (this$temperatureStatus == null) {
            if (other$temperatureStatus != null) {
                return false;
            }
        } else if (!this$temperatureStatus.equals(other$temperatureStatus)) {
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
        Object this$manageDeviceSnList = getManageDeviceSnList();
        Object other$manageDeviceSnList = other.getManageDeviceSnList();
        if (this$manageDeviceSnList == null) {
            if (other$manageDeviceSnList != null) {
                return false;
            }
        } else if (!this$manageDeviceSnList.equals(other$manageDeviceSnList)) {
            return false;
        }
        Object this$keywords = getKeywords();
        Object other$keywords = other.getKeywords();
        return this$keywords == null ? other$keywords == null : this$keywords.equals(other$keywords);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof TemperatureRecordRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $startDatetime = getStartDatetime();
        int result2 = (result * 59) + ($startDatetime == null ? 43 : $startDatetime.hashCode());
        Object $endDatetime = getEndDatetime();
        int result3 = (result2 * 59) + ($endDatetime == null ? 43 : $endDatetime.hashCode());
        Object $deptId = getDeptId();
        int result4 = (result3 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deviceSn = getDeviceSn();
        int result5 = (result4 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $temperatureStatus = getTemperatureStatus();
        int result6 = (result5 * 59) + ($temperatureStatus == null ? 43 : $temperatureStatus.hashCode());
        Object $deptIdList = getDeptIdList();
        int result7 = (result6 * 59) + ($deptIdList == null ? 43 : $deptIdList.hashCode());
        Object $manageDeptIdList = getManageDeptIdList();
        int result8 = (result7 * 59) + ($manageDeptIdList == null ? 43 : $manageDeptIdList.hashCode());
        Object $manageDeviceSnList = getManageDeviceSnList();
        int result9 = (result8 * 59) + ($manageDeviceSnList == null ? 43 : $manageDeviceSnList.hashCode());
        Object $keywords = getKeywords();
        return (result9 * 59) + ($keywords == null ? 43 : $keywords.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "TemperatureRecordRequest(paginator=" + getPaginator() + ", startDatetime=" + getStartDatetime() + ", endDatetime=" + getEndDatetime() + ", deptId=" + getDeptId() + ", deviceSn=" + getDeviceSn() + ", temperatureStatus=" + getTemperatureStatus() + ", deptIdList=" + getDeptIdList() + ", manageDeptIdList=" + getManageDeptIdList() + ", manageDeviceSnList=" + getManageDeviceSnList() + ", keywords=" + getKeywords() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public String getStartDatetime() {
        return this.startDatetime;
    }

    public String getEndDatetime() {
        return this.endDatetime;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Integer getTemperatureStatus() {
        return this.temperatureStatus;
    }

    public List<Long> getDeptIdList() {
        return this.deptIdList;
    }

    public List<String> getManageDeptIdList() {
        return this.manageDeptIdList;
    }

    public List<String> getManageDeviceSnList() {
        return this.manageDeviceSnList;
    }

    public String getKeywords() {
        return this.keywords;
    }
}
