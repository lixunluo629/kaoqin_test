package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "识别记录查询列表请求信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/VerifyRecordListRequest.class */
public class VerifyRecordListRequest extends BaseRequest {

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(name = "startTimeStr", value = "开始时间字符串，1.5.7后废弃，请及时用startTimestamp代替")
    private String startTimeStr;

    @ApiModelProperty(name = "endTimeStr", value = "结束时间字符串，1.5.7后废弃，请及时用startTimestamp代替")
    private String endTimeStr;

    @ApiModelProperty(name = "startTimestamp", value = "开始时间戳（毫秒）例：1586500125121。时间查询结果：包含查询开始时间，不包含查询结束时间")
    private Long startTimestamp;

    @ApiModelProperty(name = "endTimestamp", value = "结束时间戳（毫秒）例：1586500125121。时间查询结果：包含查询开始时间，不包含查询结束时间")
    private Long endTimestamp;

    @ApiModelProperty(name = "startDate", value = "开始时间日期例：20200410。时间查询结果：包含查询开始时间，不包含查询结束时间")
    private Integer startDate;

    @ApiModelProperty(name = "endDate", value = "结束时间日期例：20200410。时间查询结果：包含查询开始时间，不包含查询结束时间")
    private Integer endDate;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "deviceId", value = "设备id")
    private Long deviceId;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "memberName", value = "成员名称", hidden = true)
    private String memberName;

    @ApiModelProperty(name = "keyWords", value = "搜索关键字")
    private String keyWords;

    @ApiModelProperty(name = "authFlag", value = "记录类型： 0：全部:1：有权限:2：无权限")
    private Integer authFlag;

    @ApiModelProperty(name = "managerDeptIds", value = "部门id列表", hidden = true)
    private List<String> managerDeptIds;

    @ApiModelProperty(name = "deptIds", value = "部门id列表", hidden = true)
    private List<Long> deptIds;

    @ApiModelProperty(name = "deviceSns", value = "设备sn列表", hidden = true)
    private List<String> deviceSns;

    @ApiModelProperty(name = "managerDeviceSns", value = "设备sn列表", hidden = true)
    private List<String> managerDeviceSns;

    @ApiModelProperty(name = "verifyScore", value = "得分")
    private Integer verifyScore;

    @ApiModelProperty(name = "secondVerifyScore", value = "第二名得分")
    private Integer secondVerifyScore;

    @ApiModelProperty(name = "mirrorVerifyScore", value = "镜像照片识别分数")
    private Integer mirrorVerifyScore;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_APP_TYPE_KEY, value = "业务：1-魔点门禁，2-魔点考勤，3-魔点签到，4-魔点访客，5-魔点团餐，6-温控")
    private Integer appType;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

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

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public void setAuthFlag(Integer authFlag) {
        this.authFlag = authFlag;
    }

    public void setManagerDeptIds(List<String> managerDeptIds) {
        this.managerDeptIds = managerDeptIds;
    }

    public void setDeptIds(List<Long> deptIds) {
        this.deptIds = deptIds;
    }

    public void setDeviceSns(List<String> deviceSns) {
        this.deviceSns = deviceSns;
    }

    public void setManagerDeviceSns(List<String> managerDeviceSns) {
        this.managerDeviceSns = managerDeviceSns;
    }

    public void setVerifyScore(Integer verifyScore) {
        this.verifyScore = verifyScore;
    }

    public void setSecondVerifyScore(Integer secondVerifyScore) {
        this.secondVerifyScore = secondVerifyScore;
    }

    public void setMirrorVerifyScore(Integer mirrorVerifyScore) {
        this.mirrorVerifyScore = mirrorVerifyScore;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VerifyRecordListRequest)) {
            return false;
        }
        VerifyRecordListRequest other = (VerifyRecordListRequest) o;
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
        Object this$deptId = getDeptId();
        Object other$deptId = other.getDeptId();
        if (this$deptId == null) {
            if (other$deptId != null) {
                return false;
            }
        } else if (!this$deptId.equals(other$deptId)) {
            return false;
        }
        Object this$deviceId = getDeviceId();
        Object other$deviceId = other.getDeviceId();
        if (this$deviceId == null) {
            if (other$deviceId != null) {
                return false;
            }
        } else if (!this$deviceId.equals(other$deviceId)) {
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
        Object this$memberName = getMemberName();
        Object other$memberName = other.getMemberName();
        if (this$memberName == null) {
            if (other$memberName != null) {
                return false;
            }
        } else if (!this$memberName.equals(other$memberName)) {
            return false;
        }
        Object this$keyWords = getKeyWords();
        Object other$keyWords = other.getKeyWords();
        if (this$keyWords == null) {
            if (other$keyWords != null) {
                return false;
            }
        } else if (!this$keyWords.equals(other$keyWords)) {
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
        Object this$managerDeptIds = getManagerDeptIds();
        Object other$managerDeptIds = other.getManagerDeptIds();
        if (this$managerDeptIds == null) {
            if (other$managerDeptIds != null) {
                return false;
            }
        } else if (!this$managerDeptIds.equals(other$managerDeptIds)) {
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
        Object this$managerDeviceSns = getManagerDeviceSns();
        Object other$managerDeviceSns = other.getManagerDeviceSns();
        if (this$managerDeviceSns == null) {
            if (other$managerDeviceSns != null) {
                return false;
            }
        } else if (!this$managerDeviceSns.equals(other$managerDeviceSns)) {
            return false;
        }
        Object this$verifyScore = getVerifyScore();
        Object other$verifyScore = other.getVerifyScore();
        if (this$verifyScore == null) {
            if (other$verifyScore != null) {
                return false;
            }
        } else if (!this$verifyScore.equals(other$verifyScore)) {
            return false;
        }
        Object this$secondVerifyScore = getSecondVerifyScore();
        Object other$secondVerifyScore = other.getSecondVerifyScore();
        if (this$secondVerifyScore == null) {
            if (other$secondVerifyScore != null) {
                return false;
            }
        } else if (!this$secondVerifyScore.equals(other$secondVerifyScore)) {
            return false;
        }
        Object this$mirrorVerifyScore = getMirrorVerifyScore();
        Object other$mirrorVerifyScore = other.getMirrorVerifyScore();
        if (this$mirrorVerifyScore == null) {
            if (other$mirrorVerifyScore != null) {
                return false;
            }
        } else if (!this$mirrorVerifyScore.equals(other$mirrorVerifyScore)) {
            return false;
        }
        Object this$appType = getAppType();
        Object other$appType = other.getAppType();
        return this$appType == null ? other$appType == null : this$appType.equals(other$appType);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof VerifyRecordListRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $startTimeStr = getStartTimeStr();
        int result2 = (result * 59) + ($startTimeStr == null ? 43 : $startTimeStr.hashCode());
        Object $endTimeStr = getEndTimeStr();
        int result3 = (result2 * 59) + ($endTimeStr == null ? 43 : $endTimeStr.hashCode());
        Object $startTimestamp = getStartTimestamp();
        int result4 = (result3 * 59) + ($startTimestamp == null ? 43 : $startTimestamp.hashCode());
        Object $endTimestamp = getEndTimestamp();
        int result5 = (result4 * 59) + ($endTimestamp == null ? 43 : $endTimestamp.hashCode());
        Object $startDate = getStartDate();
        int result6 = (result5 * 59) + ($startDate == null ? 43 : $startDate.hashCode());
        Object $endDate = getEndDate();
        int result7 = (result6 * 59) + ($endDate == null ? 43 : $endDate.hashCode());
        Object $deptId = getDeptId();
        int result8 = (result7 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deviceId = getDeviceId();
        int result9 = (result8 * 59) + ($deviceId == null ? 43 : $deviceId.hashCode());
        Object $deviceSn = getDeviceSn();
        int result10 = (result9 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $memberName = getMemberName();
        int result11 = (result10 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $keyWords = getKeyWords();
        int result12 = (result11 * 59) + ($keyWords == null ? 43 : $keyWords.hashCode());
        Object $authFlag = getAuthFlag();
        int result13 = (result12 * 59) + ($authFlag == null ? 43 : $authFlag.hashCode());
        Object $managerDeptIds = getManagerDeptIds();
        int result14 = (result13 * 59) + ($managerDeptIds == null ? 43 : $managerDeptIds.hashCode());
        Object $deptIds = getDeptIds();
        int result15 = (result14 * 59) + ($deptIds == null ? 43 : $deptIds.hashCode());
        Object $deviceSns = getDeviceSns();
        int result16 = (result15 * 59) + ($deviceSns == null ? 43 : $deviceSns.hashCode());
        Object $managerDeviceSns = getManagerDeviceSns();
        int result17 = (result16 * 59) + ($managerDeviceSns == null ? 43 : $managerDeviceSns.hashCode());
        Object $verifyScore = getVerifyScore();
        int result18 = (result17 * 59) + ($verifyScore == null ? 43 : $verifyScore.hashCode());
        Object $secondVerifyScore = getSecondVerifyScore();
        int result19 = (result18 * 59) + ($secondVerifyScore == null ? 43 : $secondVerifyScore.hashCode());
        Object $mirrorVerifyScore = getMirrorVerifyScore();
        int result20 = (result19 * 59) + ($mirrorVerifyScore == null ? 43 : $mirrorVerifyScore.hashCode());
        Object $appType = getAppType();
        return (result20 * 59) + ($appType == null ? 43 : $appType.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "VerifyRecordListRequest(paginator=" + getPaginator() + ", startTimeStr=" + getStartTimeStr() + ", endTimeStr=" + getEndTimeStr() + ", startTimestamp=" + getStartTimestamp() + ", endTimestamp=" + getEndTimestamp() + ", startDate=" + getStartDate() + ", endDate=" + getEndDate() + ", deptId=" + getDeptId() + ", deviceId=" + getDeviceId() + ", deviceSn=" + getDeviceSn() + ", memberName=" + getMemberName() + ", keyWords=" + getKeyWords() + ", authFlag=" + getAuthFlag() + ", managerDeptIds=" + getManagerDeptIds() + ", deptIds=" + getDeptIds() + ", deviceSns=" + getDeviceSns() + ", managerDeviceSns=" + getManagerDeviceSns() + ", verifyScore=" + getVerifyScore() + ", secondVerifyScore=" + getSecondVerifyScore() + ", mirrorVerifyScore=" + getMirrorVerifyScore() + ", appType=" + getAppType() + ")";
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

    public Long getDeptId() {
        return this.deptId;
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getKeyWords() {
        return this.keyWords;
    }

    public Integer getAuthFlag() {
        return this.authFlag;
    }

    public List<String> getManagerDeptIds() {
        return this.managerDeptIds;
    }

    public List<Long> getDeptIds() {
        return this.deptIds;
    }

    public List<String> getDeviceSns() {
        return this.deviceSns;
    }

    public List<String> getManagerDeviceSns() {
        return this.managerDeviceSns;
    }

    public Integer getVerifyScore() {
        return this.verifyScore;
    }

    public Integer getSecondVerifyScore() {
        return this.secondVerifyScore;
    }

    public Integer getMirrorVerifyScore() {
        return this.mirrorVerifyScore;
    }

    public Integer getAppType() {
        return this.appType;
    }
}
