package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "识别记录导出报表请求对象")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/VerifyRecordExcelRequest.class */
public class VerifyRecordExcelRequest extends BaseRequest {

    @ApiModelProperty(name = "startTimeStr", value = "开始时间字符串，1.5.7后废弃，请及时用startTimestamp代替")
    private String startTimeStr;

    @ApiModelProperty(name = "endTimeStr", value = "结束时间字符串，1.5.7后废弃，请及时用startTimestamp代替")
    private String endTimeStr;

    @ApiModelProperty(name = "startTimestamp", value = "开始时间戳例：1586500125121")
    private Long startTimestamp;

    @ApiModelProperty(name = "endTimestamp", value = "结束时间戳例：1586500125121")
    private Long endTimestamp;

    @ApiModelProperty(name = "startDate", value = "开始时间日期例：20200410")
    private Integer startDate;

    @ApiModelProperty(name = "endDate", value = "结束时间日期例：20200410")
    private Integer endDate;

    @ApiModelProperty(name = "deptIds", value = "部门id集合，多个之间逗号隔开")
    private String deptIds;

    @ApiModelProperty(name = "deviceIds", value = "设备id")
    private String deviceSns;

    @ApiModelProperty(name = "memberIds", value = "成员id，多个之间逗号隔开")
    private String memberIds;

    @ApiModelProperty(name = "deptIdList", value = "部门id集合", hidden = true)
    private List<Long> deptIdList;

    @ApiModelProperty(name = "deviceSnList", value = "设备sn", hidden = true)
    private List<String> deviceSnList;

    @ApiModelProperty(name = "memberIdList", value = "成员id", hidden = true)
    private List<Long> memberIdList;

    @ApiModelProperty(name = "authFlag", value = "记录类型： 0：全部:1：有权限:2：无权限")
    private Integer authFlag;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_APP_TYPE_KEY, value = "业务：1-魔点门禁，2-魔点考勤，3-魔点签到，4-魔点访客，5-魔点团餐")
    private Integer appType;

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public void setEndTimestamp(Long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }

    public void setDeptIds(String deptIds) {
        this.deptIds = deptIds;
    }

    public void setDeviceSns(String deviceSns) {
        this.deviceSns = deviceSns;
    }

    public void setMemberIds(String memberIds) {
        this.memberIds = memberIds;
    }

    public void setDeptIdList(List<Long> deptIdList) {
        this.deptIdList = deptIdList;
    }

    public void setDeviceSnList(List<String> deviceSnList) {
        this.deviceSnList = deviceSnList;
    }

    public void setMemberIdList(List<Long> memberIdList) {
        this.memberIdList = memberIdList;
    }

    public void setAuthFlag(Integer authFlag) {
        this.authFlag = authFlag;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VerifyRecordExcelRequest)) {
            return false;
        }
        VerifyRecordExcelRequest other = (VerifyRecordExcelRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$startTimestamp = getStartTimestamp();
        Object other$startTimestamp = other.getStartTimestamp();
        if (this$startTimestamp == null) {
            if (other$startTimestamp != null) {
                return false;
            }
        } else if (!this$startTimestamp.equals(other$startTimestamp)) {
            return false;
        }
        Object this$endTimestamp = getEndTimestamp();
        Object other$endTimestamp = other.getEndTimestamp();
        if (this$endTimestamp == null) {
            if (other$endTimestamp != null) {
                return false;
            }
        } else if (!this$endTimestamp.equals(other$endTimestamp)) {
            return false;
        }
        Object this$startDate = getStartDate();
        Object other$startDate = other.getStartDate();
        if (this$startDate == null) {
            if (other$startDate != null) {
                return false;
            }
        } else if (!this$startDate.equals(other$startDate)) {
            return false;
        }
        Object this$endDate = getEndDate();
        Object other$endDate = other.getEndDate();
        if (this$endDate == null) {
            if (other$endDate != null) {
                return false;
            }
        } else if (!this$endDate.equals(other$endDate)) {
            return false;
        }
        Object this$deptIds = getDeptIds();
        Object other$deptIds = other.getDeptIds();
        if (this$deptIds == null) {
            if (other$deptIds != null) {
                return false;
            }
        } else if (!this$deptIds.equals(other$deptIds)) {
            return false;
        }
        Object this$deviceSns = getDeviceSns();
        Object other$deviceSns = other.getDeviceSns();
        if (this$deviceSns == null) {
            if (other$deviceSns != null) {
                return false;
            }
        } else if (!this$deviceSns.equals(other$deviceSns)) {
            return false;
        }
        Object this$memberIds = getMemberIds();
        Object other$memberIds = other.getMemberIds();
        if (this$memberIds == null) {
            if (other$memberIds != null) {
                return false;
            }
        } else if (!this$memberIds.equals(other$memberIds)) {
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
        Object this$deviceSnList = getDeviceSnList();
        Object other$deviceSnList = other.getDeviceSnList();
        if (this$deviceSnList == null) {
            if (other$deviceSnList != null) {
                return false;
            }
        } else if (!this$deviceSnList.equals(other$deviceSnList)) {
            return false;
        }
        Object this$memberIdList = getMemberIdList();
        Object other$memberIdList = other.getMemberIdList();
        if (this$memberIdList == null) {
            if (other$memberIdList != null) {
                return false;
            }
        } else if (!this$memberIdList.equals(other$memberIdList)) {
            return false;
        }
        Object this$authFlag = getAuthFlag();
        Object other$authFlag = other.getAuthFlag();
        if (this$authFlag == null) {
            if (other$authFlag != null) {
                return false;
            }
        } else if (!this$authFlag.equals(other$authFlag)) {
            return false;
        }
        Object this$appType = getAppType();
        Object other$appType = other.getAppType();
        return this$appType == null ? other$appType == null : this$appType.equals(other$appType);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof VerifyRecordExcelRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $startTimeStr = getStartTimeStr();
        int result = (1 * 59) + ($startTimeStr == null ? 43 : $startTimeStr.hashCode());
        Object $endTimeStr = getEndTimeStr();
        int result2 = (result * 59) + ($endTimeStr == null ? 43 : $endTimeStr.hashCode());
        Object $startTimestamp = getStartTimestamp();
        int result3 = (result2 * 59) + ($startTimestamp == null ? 43 : $startTimestamp.hashCode());
        Object $endTimestamp = getEndTimestamp();
        int result4 = (result3 * 59) + ($endTimestamp == null ? 43 : $endTimestamp.hashCode());
        Object $startDate = getStartDate();
        int result5 = (result4 * 59) + ($startDate == null ? 43 : $startDate.hashCode());
        Object $endDate = getEndDate();
        int result6 = (result5 * 59) + ($endDate == null ? 43 : $endDate.hashCode());
        Object $deptIds = getDeptIds();
        int result7 = (result6 * 59) + ($deptIds == null ? 43 : $deptIds.hashCode());
        Object $deviceSns = getDeviceSns();
        int result8 = (result7 * 59) + ($deviceSns == null ? 43 : $deviceSns.hashCode());
        Object $memberIds = getMemberIds();
        int result9 = (result8 * 59) + ($memberIds == null ? 43 : $memberIds.hashCode());
        Object $deptIdList = getDeptIdList();
        int result10 = (result9 * 59) + ($deptIdList == null ? 43 : $deptIdList.hashCode());
        Object $deviceSnList = getDeviceSnList();
        int result11 = (result10 * 59) + ($deviceSnList == null ? 43 : $deviceSnList.hashCode());
        Object $memberIdList = getMemberIdList();
        int result12 = (result11 * 59) + ($memberIdList == null ? 43 : $memberIdList.hashCode());
        Object $authFlag = getAuthFlag();
        int result13 = (result12 * 59) + ($authFlag == null ? 43 : $authFlag.hashCode());
        Object $appType = getAppType();
        return (result13 * 59) + ($appType == null ? 43 : $appType.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "VerifyRecordExcelRequest(startTimeStr=" + getStartTimeStr() + ", endTimeStr=" + getEndTimeStr() + ", startTimestamp=" + getStartTimestamp() + ", endTimestamp=" + getEndTimestamp() + ", startDate=" + getStartDate() + ", endDate=" + getEndDate() + ", deptIds=" + getDeptIds() + ", deviceSns=" + getDeviceSns() + ", memberIds=" + getMemberIds() + ", deptIdList=" + getDeptIdList() + ", deviceSnList=" + getDeviceSnList() + ", memberIdList=" + getMemberIdList() + ", authFlag=" + getAuthFlag() + ", appType=" + getAppType() + ")";
    }

    public String getStartTimeStr() {
        return this.startTimeStr;
    }

    public String getEndTimeStr() {
        return this.endTimeStr;
    }

    public Long getStartTimestamp() {
        return this.startTimestamp;
    }

    public Long getEndTimestamp() {
        return this.endTimestamp;
    }

    public Integer getStartDate() {
        return this.startDate;
    }

    public Integer getEndDate() {
        return this.endDate;
    }

    public String getDeptIds() {
        return this.deptIds;
    }

    public String getDeviceSns() {
        return this.deviceSns;
    }

    public String getMemberIds() {
        return this.memberIds;
    }

    public List<Long> getDeptIdList() {
        return this.deptIdList;
    }

    public List<String> getDeviceSnList() {
        return this.deviceSnList;
    }

    public List<Long> getMemberIdList() {
        return this.memberIdList;
    }

    public Integer getAuthFlag() {
        return this.authFlag;
    }

    public Integer getAppType() {
        return this.appType;
    }
}
