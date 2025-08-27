package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.model.dto.AttendanceGroupTimeDto;
import com.moredian.onpremise.core.model.dto.DeviceDto;
import com.moredian.onpremise.core.model.dto.GroupMemberDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "保存考勤组请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveAttendanceGroupRequest.class */
public class SaveAttendanceGroupRequest extends BaseRequest {
    private static final long serialVersionUID = 1216264818413820300L;

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id,新增时不传，修改时必填")
    private Long attendanceGroupId;

    @ApiModelProperty(name = "groupName", value = "考勤组名称")
    private String groupName;

    @ApiModelProperty(name = "groupType", value = "考勤组类型：1-固定排班，2-手动排班，3-自由打卡")
    private Integer groupType;

    @ApiModelProperty(name = "groupStartDate", value = "考勤组有效期开始日期，只在手动排班时必填")
    private String groupStartDate;

    @ApiModelProperty(name = "groupEndDate", value = "考勤组有效期结束日期，只在手动排班时必填")
    private String groupEndDate;

    @ApiModelProperty(name = "speechContent", value = "语音播报文本内容")
    private String speechContent;

    @ApiModelProperty(name = "showContent", value = "界面提示文本内容")
    private String showContent;

    @ApiModelProperty(name = "memberInfo", value = "考勤组成员集合")
    private List<GroupMemberDto> memberInfo;

    @ApiModelProperty(name = "attendanceBeginDeviceInfo", value = "上班打卡设备信息列表")
    private List<DeviceDto> attendanceBeginDeviceInfo;

    @ApiModelProperty(name = "groupTimeInfo", value = "上班打卡时间信息列表")
    private List<AttendanceGroupTimeDto> groupTimeInfo;

    @ApiModelProperty(name = "attendanceEndDeviceInfo", value = "下班打卡设备信息列表")
    private List<DeviceDto> attendanceEndDeviceInfo;

    @ApiModelProperty(name = "doorFlag", value = "打卡成功是否开门，0-否；1-是")
    private Integer doorFlag;

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public void setGroupStartDate(String groupStartDate) {
        this.groupStartDate = groupStartDate;
    }

    public void setGroupEndDate(String groupEndDate) {
        this.groupEndDate = groupEndDate;
    }

    public void setSpeechContent(String speechContent) {
        this.speechContent = speechContent;
    }

    public void setShowContent(String showContent) {
        this.showContent = showContent;
    }

    public void setMemberInfo(List<GroupMemberDto> memberInfo) {
        this.memberInfo = memberInfo;
    }

    public void setAttendanceBeginDeviceInfo(List<DeviceDto> attendanceBeginDeviceInfo) {
        this.attendanceBeginDeviceInfo = attendanceBeginDeviceInfo;
    }

    public void setGroupTimeInfo(List<AttendanceGroupTimeDto> groupTimeInfo) {
        this.groupTimeInfo = groupTimeInfo;
    }

    public void setAttendanceEndDeviceInfo(List<DeviceDto> attendanceEndDeviceInfo) {
        this.attendanceEndDeviceInfo = attendanceEndDeviceInfo;
    }

    public void setDoorFlag(Integer doorFlag) {
        this.doorFlag = doorFlag;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveAttendanceGroupRequest)) {
            return false;
        }
        SaveAttendanceGroupRequest other = (SaveAttendanceGroupRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$attendanceGroupId = getAttendanceGroupId();
        Object other$attendanceGroupId = other.getAttendanceGroupId();
        if (this$attendanceGroupId == null) {
            if (other$attendanceGroupId != null) {
                return false;
            }
        } else if (!this$attendanceGroupId.equals(other$attendanceGroupId)) {
            return false;
        }
        Object this$groupName = getGroupName();
        Object other$groupName = other.getGroupName();
        if (this$groupName == null) {
            if (other$groupName != null) {
                return false;
            }
        } else if (!this$groupName.equals(other$groupName)) {
            return false;
        }
        Object this$groupType = getGroupType();
        Object other$groupType = other.getGroupType();
        if (this$groupType == null) {
            if (other$groupType != null) {
                return false;
            }
        } else if (!this$groupType.equals(other$groupType)) {
            return false;
        }
        Object this$groupStartDate = getGroupStartDate();
        Object other$groupStartDate = other.getGroupStartDate();
        if (this$groupStartDate == null) {
            if (other$groupStartDate != null) {
                return false;
            }
        } else if (!this$groupStartDate.equals(other$groupStartDate)) {
            return false;
        }
        Object this$groupEndDate = getGroupEndDate();
        Object other$groupEndDate = other.getGroupEndDate();
        if (this$groupEndDate == null) {
            if (other$groupEndDate != null) {
                return false;
            }
        } else if (!this$groupEndDate.equals(other$groupEndDate)) {
            return false;
        }
        Object this$speechContent = getSpeechContent();
        Object other$speechContent = other.getSpeechContent();
        if (this$speechContent == null) {
            if (other$speechContent != null) {
                return false;
            }
        } else if (!this$speechContent.equals(other$speechContent)) {
            return false;
        }
        Object this$showContent = getShowContent();
        Object other$showContent = other.getShowContent();
        if (this$showContent == null) {
            if (other$showContent != null) {
                return false;
            }
        } else if (!this$showContent.equals(other$showContent)) {
            return false;
        }
        Object this$memberInfo = getMemberInfo();
        Object other$memberInfo = other.getMemberInfo();
        if (this$memberInfo == null) {
            if (other$memberInfo != null) {
                return false;
            }
        } else if (!this$memberInfo.equals(other$memberInfo)) {
            return false;
        }
        Object this$attendanceBeginDeviceInfo = getAttendanceBeginDeviceInfo();
        Object other$attendanceBeginDeviceInfo = other.getAttendanceBeginDeviceInfo();
        if (this$attendanceBeginDeviceInfo == null) {
            if (other$attendanceBeginDeviceInfo != null) {
                return false;
            }
        } else if (!this$attendanceBeginDeviceInfo.equals(other$attendanceBeginDeviceInfo)) {
            return false;
        }
        Object this$groupTimeInfo = getGroupTimeInfo();
        Object other$groupTimeInfo = other.getGroupTimeInfo();
        if (this$groupTimeInfo == null) {
            if (other$groupTimeInfo != null) {
                return false;
            }
        } else if (!this$groupTimeInfo.equals(other$groupTimeInfo)) {
            return false;
        }
        Object this$attendanceEndDeviceInfo = getAttendanceEndDeviceInfo();
        Object other$attendanceEndDeviceInfo = other.getAttendanceEndDeviceInfo();
        if (this$attendanceEndDeviceInfo == null) {
            if (other$attendanceEndDeviceInfo != null) {
                return false;
            }
        } else if (!this$attendanceEndDeviceInfo.equals(other$attendanceEndDeviceInfo)) {
            return false;
        }
        Object this$doorFlag = getDoorFlag();
        Object other$doorFlag = other.getDoorFlag();
        return this$doorFlag == null ? other$doorFlag == null : this$doorFlag.equals(other$doorFlag);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveAttendanceGroupRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $attendanceGroupId = getAttendanceGroupId();
        int result = (1 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        Object $groupName = getGroupName();
        int result2 = (result * 59) + ($groupName == null ? 43 : $groupName.hashCode());
        Object $groupType = getGroupType();
        int result3 = (result2 * 59) + ($groupType == null ? 43 : $groupType.hashCode());
        Object $groupStartDate = getGroupStartDate();
        int result4 = (result3 * 59) + ($groupStartDate == null ? 43 : $groupStartDate.hashCode());
        Object $groupEndDate = getGroupEndDate();
        int result5 = (result4 * 59) + ($groupEndDate == null ? 43 : $groupEndDate.hashCode());
        Object $speechContent = getSpeechContent();
        int result6 = (result5 * 59) + ($speechContent == null ? 43 : $speechContent.hashCode());
        Object $showContent = getShowContent();
        int result7 = (result6 * 59) + ($showContent == null ? 43 : $showContent.hashCode());
        Object $memberInfo = getMemberInfo();
        int result8 = (result7 * 59) + ($memberInfo == null ? 43 : $memberInfo.hashCode());
        Object $attendanceBeginDeviceInfo = getAttendanceBeginDeviceInfo();
        int result9 = (result8 * 59) + ($attendanceBeginDeviceInfo == null ? 43 : $attendanceBeginDeviceInfo.hashCode());
        Object $groupTimeInfo = getGroupTimeInfo();
        int result10 = (result9 * 59) + ($groupTimeInfo == null ? 43 : $groupTimeInfo.hashCode());
        Object $attendanceEndDeviceInfo = getAttendanceEndDeviceInfo();
        int result11 = (result10 * 59) + ($attendanceEndDeviceInfo == null ? 43 : $attendanceEndDeviceInfo.hashCode());
        Object $doorFlag = getDoorFlag();
        return (result11 * 59) + ($doorFlag == null ? 43 : $doorFlag.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveAttendanceGroupRequest(attendanceGroupId=" + getAttendanceGroupId() + ", groupName=" + getGroupName() + ", groupType=" + getGroupType() + ", groupStartDate=" + getGroupStartDate() + ", groupEndDate=" + getGroupEndDate() + ", speechContent=" + getSpeechContent() + ", showContent=" + getShowContent() + ", memberInfo=" + getMemberInfo() + ", attendanceBeginDeviceInfo=" + getAttendanceBeginDeviceInfo() + ", groupTimeInfo=" + getGroupTimeInfo() + ", attendanceEndDeviceInfo=" + getAttendanceEndDeviceInfo() + ", doorFlag=" + getDoorFlag() + ")";
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Integer getGroupType() {
        return this.groupType;
    }

    public String getGroupStartDate() {
        return this.groupStartDate;
    }

    public String getGroupEndDate() {
        return this.groupEndDate;
    }

    public String getSpeechContent() {
        return this.speechContent;
    }

    public String getShowContent() {
        return this.showContent;
    }

    public List<GroupMemberDto> getMemberInfo() {
        return this.memberInfo;
    }

    public List<DeviceDto> getAttendanceBeginDeviceInfo() {
        return this.attendanceBeginDeviceInfo;
    }

    public List<AttendanceGroupTimeDto> getGroupTimeInfo() {
        return this.groupTimeInfo;
    }

    public List<DeviceDto> getAttendanceEndDeviceInfo() {
        return this.attendanceEndDeviceInfo;
    }

    public Integer getDoorFlag() {
        return this.doorFlag;
    }
}
