package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.model.dto.DeviceDto;
import com.moredian.onpremise.core.model.dto.GroupAuthDto;
import com.moredian.onpremise.core.model.dto.GroupMemberDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "权限组信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/GroupAuthResponse.class */
public class GroupAuthResponse implements Serializable {

    @ApiModelProperty(name = "groupId", value = "权限组id")
    private Long groupId;

    @ApiModelProperty(name = "groupCode", value = "权限组code")
    private String groupCode;

    @ApiModelProperty(name = "groupName", value = "权限组名称")
    private String groupName;

    @ApiModelProperty(name = "groupMembers", value = "权限组允许使用成员")
    private List<GroupMemberDto> groupMembers;

    @ApiModelProperty(name = "groupAuths", value = "权限组允许使用权限范围")
    private List<GroupAuthDto> groupAuths;

    @ApiModelProperty(name = "groupDevices", value = "权限组允许使用设备")
    private List<DeviceDto> groupDevices;

    @ApiModelProperty(name = "defaultFlag", value = "默认群组标识：1-是，0-否")
    private Integer defaultFlag;

    @ApiModelProperty(name = "allMemberFlag", value = "是否是全员组：1-是，0-否")
    private Integer allMemberFlag;

    @ApiModelProperty(name = "permanentFlag", value = "是否永久标识：1-是，0-否")
    private Integer permanentFlag;

    @ApiModelProperty(name = "cycleFlag", value = "是否重复每天：1-是，0-否")
    private Integer cycleFlag;

    @ApiModelProperty(name = "allDayFlag", value = "全天生效标识：1-是，0-否")
    private Integer allDayFlag;

    @ApiModelProperty(name = "showContent", value = "界面提示文本内容")
    private String showContent;

    @ApiModelProperty(name = "speechContent", value = "语音播报文本内容")
    private String speechContent;

    @ApiModelProperty(name = "rsOutput", value = "485输出")
    private String rsOutput;

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupMembers(List<GroupMemberDto> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public void setGroupAuths(List<GroupAuthDto> groupAuths) {
        this.groupAuths = groupAuths;
    }

    public void setGroupDevices(List<DeviceDto> groupDevices) {
        this.groupDevices = groupDevices;
    }

    public void setDefaultFlag(Integer defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public void setAllMemberFlag(Integer allMemberFlag) {
        this.allMemberFlag = allMemberFlag;
    }

    public void setPermanentFlag(Integer permanentFlag) {
        this.permanentFlag = permanentFlag;
    }

    public void setCycleFlag(Integer cycleFlag) {
        this.cycleFlag = cycleFlag;
    }

    public void setAllDayFlag(Integer allDayFlag) {
        this.allDayFlag = allDayFlag;
    }

    public void setShowContent(String showContent) {
        this.showContent = showContent;
    }

    public void setSpeechContent(String speechContent) {
        this.speechContent = speechContent;
    }

    public void setRsOutput(String rsOutput) {
        this.rsOutput = rsOutput;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GroupAuthResponse)) {
            return false;
        }
        GroupAuthResponse other = (GroupAuthResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$groupId = getGroupId();
        Object other$groupId = other.getGroupId();
        if (this$groupId == null) {
            if (other$groupId != null) {
                return false;
            }
        } else if (!this$groupId.equals(other$groupId)) {
            return false;
        }
        Object this$groupCode = getGroupCode();
        Object other$groupCode = other.getGroupCode();
        if (this$groupCode == null) {
            if (other$groupCode != null) {
                return false;
            }
        } else if (!this$groupCode.equals(other$groupCode)) {
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
        Object this$groupMembers = getGroupMembers();
        Object other$groupMembers = other.getGroupMembers();
        if (this$groupMembers == null) {
            if (other$groupMembers != null) {
                return false;
            }
        } else if (!this$groupMembers.equals(other$groupMembers)) {
            return false;
        }
        Object this$groupAuths = getGroupAuths();
        Object other$groupAuths = other.getGroupAuths();
        if (this$groupAuths == null) {
            if (other$groupAuths != null) {
                return false;
            }
        } else if (!this$groupAuths.equals(other$groupAuths)) {
            return false;
        }
        Object this$groupDevices = getGroupDevices();
        Object other$groupDevices = other.getGroupDevices();
        if (this$groupDevices == null) {
            if (other$groupDevices != null) {
                return false;
            }
        } else if (!this$groupDevices.equals(other$groupDevices)) {
            return false;
        }
        Object this$defaultFlag = getDefaultFlag();
        Object other$defaultFlag = other.getDefaultFlag();
        if (this$defaultFlag == null) {
            if (other$defaultFlag != null) {
                return false;
            }
        } else if (!this$defaultFlag.equals(other$defaultFlag)) {
            return false;
        }
        Object this$allMemberFlag = getAllMemberFlag();
        Object other$allMemberFlag = other.getAllMemberFlag();
        if (this$allMemberFlag == null) {
            if (other$allMemberFlag != null) {
                return false;
            }
        } else if (!this$allMemberFlag.equals(other$allMemberFlag)) {
            return false;
        }
        Object this$permanentFlag = getPermanentFlag();
        Object other$permanentFlag = other.getPermanentFlag();
        if (this$permanentFlag == null) {
            if (other$permanentFlag != null) {
                return false;
            }
        } else if (!this$permanentFlag.equals(other$permanentFlag)) {
            return false;
        }
        Object this$cycleFlag = getCycleFlag();
        Object other$cycleFlag = other.getCycleFlag();
        if (this$cycleFlag == null) {
            if (other$cycleFlag != null) {
                return false;
            }
        } else if (!this$cycleFlag.equals(other$cycleFlag)) {
            return false;
        }
        Object this$allDayFlag = getAllDayFlag();
        Object other$allDayFlag = other.getAllDayFlag();
        if (this$allDayFlag == null) {
            if (other$allDayFlag != null) {
                return false;
            }
        } else if (!this$allDayFlag.equals(other$allDayFlag)) {
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
        Object this$speechContent = getSpeechContent();
        Object other$speechContent = other.getSpeechContent();
        if (this$speechContent == null) {
            if (other$speechContent != null) {
                return false;
            }
        } else if (!this$speechContent.equals(other$speechContent)) {
            return false;
        }
        Object this$rsOutput = getRsOutput();
        Object other$rsOutput = other.getRsOutput();
        return this$rsOutput == null ? other$rsOutput == null : this$rsOutput.equals(other$rsOutput);
    }

    protected boolean canEqual(Object other) {
        return other instanceof GroupAuthResponse;
    }

    public int hashCode() {
        Object $groupId = getGroupId();
        int result = (1 * 59) + ($groupId == null ? 43 : $groupId.hashCode());
        Object $groupCode = getGroupCode();
        int result2 = (result * 59) + ($groupCode == null ? 43 : $groupCode.hashCode());
        Object $groupName = getGroupName();
        int result3 = (result2 * 59) + ($groupName == null ? 43 : $groupName.hashCode());
        Object $groupMembers = getGroupMembers();
        int result4 = (result3 * 59) + ($groupMembers == null ? 43 : $groupMembers.hashCode());
        Object $groupAuths = getGroupAuths();
        int result5 = (result4 * 59) + ($groupAuths == null ? 43 : $groupAuths.hashCode());
        Object $groupDevices = getGroupDevices();
        int result6 = (result5 * 59) + ($groupDevices == null ? 43 : $groupDevices.hashCode());
        Object $defaultFlag = getDefaultFlag();
        int result7 = (result6 * 59) + ($defaultFlag == null ? 43 : $defaultFlag.hashCode());
        Object $allMemberFlag = getAllMemberFlag();
        int result8 = (result7 * 59) + ($allMemberFlag == null ? 43 : $allMemberFlag.hashCode());
        Object $permanentFlag = getPermanentFlag();
        int result9 = (result8 * 59) + ($permanentFlag == null ? 43 : $permanentFlag.hashCode());
        Object $cycleFlag = getCycleFlag();
        int result10 = (result9 * 59) + ($cycleFlag == null ? 43 : $cycleFlag.hashCode());
        Object $allDayFlag = getAllDayFlag();
        int result11 = (result10 * 59) + ($allDayFlag == null ? 43 : $allDayFlag.hashCode());
        Object $showContent = getShowContent();
        int result12 = (result11 * 59) + ($showContent == null ? 43 : $showContent.hashCode());
        Object $speechContent = getSpeechContent();
        int result13 = (result12 * 59) + ($speechContent == null ? 43 : $speechContent.hashCode());
        Object $rsOutput = getRsOutput();
        return (result13 * 59) + ($rsOutput == null ? 43 : $rsOutput.hashCode());
    }

    public String toString() {
        return "GroupAuthResponse(groupId=" + getGroupId() + ", groupCode=" + getGroupCode() + ", groupName=" + getGroupName() + ", groupMembers=" + getGroupMembers() + ", groupAuths=" + getGroupAuths() + ", groupDevices=" + getGroupDevices() + ", defaultFlag=" + getDefaultFlag() + ", allMemberFlag=" + getAllMemberFlag() + ", permanentFlag=" + getPermanentFlag() + ", cycleFlag=" + getCycleFlag() + ", allDayFlag=" + getAllDayFlag() + ", showContent=" + getShowContent() + ", speechContent=" + getSpeechContent() + ", rsOutput=" + getRsOutput() + ")";
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public String getGroupCode() {
        return this.groupCode;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public List<GroupMemberDto> getGroupMembers() {
        return this.groupMembers;
    }

    public List<GroupAuthDto> getGroupAuths() {
        return this.groupAuths;
    }

    public List<DeviceDto> getGroupDevices() {
        return this.groupDevices;
    }

    public Integer getDefaultFlag() {
        return this.defaultFlag;
    }

    public Integer getAllMemberFlag() {
        return this.allMemberFlag;
    }

    public Integer getPermanentFlag() {
        return this.permanentFlag;
    }

    public Integer getCycleFlag() {
        return this.cycleFlag;
    }

    public Integer getAllDayFlag() {
        return this.allDayFlag;
    }

    public String getShowContent() {
        return this.showContent;
    }

    public String getSpeechContent() {
        return this.speechContent;
    }

    public String getRsOutput() {
        return this.rsOutput;
    }
}
