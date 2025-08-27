package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.model.info.SyncAttendanceGroupTimeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(description = "终端同步考勤组响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncAttendanceGroupResponse.class */
public class TerminalSyncAttendanceGroupResponse implements Serializable {

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id")
    private Long attendanceGroupId;

    @ApiModelProperty(name = "groupTimeInfo", value = "考勤组时间范围")
    private List<SyncAttendanceGroupTimeInfo> groupTimeInfo;

    @ApiModelProperty(name = "deleteOrNot", value = "是否删除：1-删除，0-保留", hidden = true)
    private Integer deleteOrNot;

    @ApiModelProperty(name = "gmtCreate", value = "创建时间", hidden = true)
    private Date gmtCreate;

    @ApiModelProperty(name = "gmtModify", value = "更新时间", hidden = true)
    private Date gmtModify;

    @ApiModelProperty(name = "speechContent", value = "语音播报文本内容")
    private String speechContent;

    @ApiModelProperty(name = "showContent", value = "界面提示文本内容")
    private String showContent;

    @ApiModelProperty(name = "groupType", value = "考勤组类型")
    private Integer groupType;

    @ApiModelProperty(name = "doorFlag", value = "打卡成功是否开门，0-否；1-是")
    private Integer doorFlag;

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public void setGroupTimeInfo(List<SyncAttendanceGroupTimeInfo> groupTimeInfo) {
        this.groupTimeInfo = groupTimeInfo;
    }

    public void setDeleteOrNot(Integer deleteOrNot) {
        this.deleteOrNot = deleteOrNot;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public void setSpeechContent(String speechContent) {
        this.speechContent = speechContent;
    }

    public void setShowContent(String showContent) {
        this.showContent = showContent;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public void setDoorFlag(Integer doorFlag) {
        this.doorFlag = doorFlag;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncAttendanceGroupResponse)) {
            return false;
        }
        TerminalSyncAttendanceGroupResponse other = (TerminalSyncAttendanceGroupResponse) o;
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
        Object this$groupTimeInfo = getGroupTimeInfo();
        Object other$groupTimeInfo = other.getGroupTimeInfo();
        if (this$groupTimeInfo == null) {
            if (other$groupTimeInfo != null) {
                return false;
            }
        } else if (!this$groupTimeInfo.equals(other$groupTimeInfo)) {
            return false;
        }
        Object this$deleteOrNot = getDeleteOrNot();
        Object other$deleteOrNot = other.getDeleteOrNot();
        if (this$deleteOrNot == null) {
            if (other$deleteOrNot != null) {
                return false;
            }
        } else if (!this$deleteOrNot.equals(other$deleteOrNot)) {
            return false;
        }
        Object this$gmtCreate = getGmtCreate();
        Object other$gmtCreate = other.getGmtCreate();
        if (this$gmtCreate == null) {
            if (other$gmtCreate != null) {
                return false;
            }
        } else if (!this$gmtCreate.equals(other$gmtCreate)) {
            return false;
        }
        Object this$gmtModify = getGmtModify();
        Object other$gmtModify = other.getGmtModify();
        if (this$gmtModify == null) {
            if (other$gmtModify != null) {
                return false;
            }
        } else if (!this$gmtModify.equals(other$gmtModify)) {
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
        Object this$groupType = getGroupType();
        Object other$groupType = other.getGroupType();
        if (this$groupType == null) {
            if (other$groupType != null) {
                return false;
            }
        } else if (!this$groupType.equals(other$groupType)) {
            return false;
        }
        Object this$doorFlag = getDoorFlag();
        Object other$doorFlag = other.getDoorFlag();
        return this$doorFlag == null ? other$doorFlag == null : this$doorFlag.equals(other$doorFlag);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncAttendanceGroupResponse;
    }

    public int hashCode() {
        Object $attendanceGroupId = getAttendanceGroupId();
        int result = (1 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        Object $groupTimeInfo = getGroupTimeInfo();
        int result2 = (result * 59) + ($groupTimeInfo == null ? 43 : $groupTimeInfo.hashCode());
        Object $deleteOrNot = getDeleteOrNot();
        int result3 = (result2 * 59) + ($deleteOrNot == null ? 43 : $deleteOrNot.hashCode());
        Object $gmtCreate = getGmtCreate();
        int result4 = (result3 * 59) + ($gmtCreate == null ? 43 : $gmtCreate.hashCode());
        Object $gmtModify = getGmtModify();
        int result5 = (result4 * 59) + ($gmtModify == null ? 43 : $gmtModify.hashCode());
        Object $speechContent = getSpeechContent();
        int result6 = (result5 * 59) + ($speechContent == null ? 43 : $speechContent.hashCode());
        Object $showContent = getShowContent();
        int result7 = (result6 * 59) + ($showContent == null ? 43 : $showContent.hashCode());
        Object $groupType = getGroupType();
        int result8 = (result7 * 59) + ($groupType == null ? 43 : $groupType.hashCode());
        Object $doorFlag = getDoorFlag();
        return (result8 * 59) + ($doorFlag == null ? 43 : $doorFlag.hashCode());
    }

    public String toString() {
        return "TerminalSyncAttendanceGroupResponse(attendanceGroupId=" + getAttendanceGroupId() + ", groupTimeInfo=" + getGroupTimeInfo() + ", deleteOrNot=" + getDeleteOrNot() + ", gmtCreate=" + getGmtCreate() + ", gmtModify=" + getGmtModify() + ", speechContent=" + getSpeechContent() + ", showContent=" + getShowContent() + ", groupType=" + getGroupType() + ", doorFlag=" + getDoorFlag() + ")";
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public List<SyncAttendanceGroupTimeInfo> getGroupTimeInfo() {
        return this.groupTimeInfo;
    }

    public Integer getDeleteOrNot() {
        return this.deleteOrNot;
    }

    public Date getGmtCreate() {
        return this.gmtCreate;
    }

    public Date getGmtModify() {
        return this.gmtModify;
    }

    public String getSpeechContent() {
        return this.speechContent;
    }

    public String getShowContent() {
        return this.showContent;
    }

    public Integer getGroupType() {
        return this.groupType;
    }

    public Integer getDoorFlag() {
        return this.doorFlag;
    }
}
