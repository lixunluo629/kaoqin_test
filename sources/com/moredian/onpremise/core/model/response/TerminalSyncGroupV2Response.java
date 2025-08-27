package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(description = "终端同步权限组响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncGroupV2Response.class */
public class TerminalSyncGroupV2Response implements Serializable {
    private static final long serialVersionUID = 3944419484998318888L;

    @ApiModelProperty(name = "groupId", value = "权限组id")
    private Long groupId;

    @ApiModelProperty(name = "groupName", value = "权限组名称")
    private String groupName;

    @ApiModelProperty(name = "groupAuths", value = "权限组权限范围")
    private List<TerminalGroupAuthSyncResponse> groupAuths;

    @ApiModelProperty(name = "allMemberFlag", value = "是否是全员组：1-是，0-否；全员组权限组不需要判断权限，识别成功就认为拥有该权限组权限")
    private Integer allMemberFlag;

    @ApiModelProperty(name = "permanentFlag", value = "重复每天标识：1-是，0-否;永久生效权限组不需要判断权限通行时间")
    private Integer permanentFlag;

    @ApiModelProperty(name = "validityPeriodFlag", value = "永久生效标识：1-是，0-否")
    private Integer validityPeriodFlag;

    @ApiModelProperty(name = "speechContent", value = "语音播报文本内容")
    private String speechContent;

    @ApiModelProperty(name = "showContent", value = "界面提示文本内容")
    private String showContent;

    @ApiModelProperty(name = "gmtCreate", value = "创建时间", hidden = true)
    private Date gmtCreate;

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupAuths(List<TerminalGroupAuthSyncResponse> groupAuths) {
        this.groupAuths = groupAuths;
    }

    public void setAllMemberFlag(Integer allMemberFlag) {
        this.allMemberFlag = allMemberFlag;
    }

    public void setPermanentFlag(Integer permanentFlag) {
        this.permanentFlag = permanentFlag;
    }

    public void setValidityPeriodFlag(Integer validityPeriodFlag) {
        this.validityPeriodFlag = validityPeriodFlag;
    }

    public void setSpeechContent(String speechContent) {
        this.speechContent = speechContent;
    }

    public void setShowContent(String showContent) {
        this.showContent = showContent;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncGroupV2Response)) {
            return false;
        }
        TerminalSyncGroupV2Response other = (TerminalSyncGroupV2Response) o;
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
        Object this$groupName = getGroupName();
        Object other$groupName = other.getGroupName();
        if (this$groupName == null) {
            if (other$groupName != null) {
                return false;
            }
        } else if (!this$groupName.equals(other$groupName)) {
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
        Object this$validityPeriodFlag = getValidityPeriodFlag();
        Object other$validityPeriodFlag = other.getValidityPeriodFlag();
        if (this$validityPeriodFlag == null) {
            if (other$validityPeriodFlag != null) {
                return false;
            }
        } else if (!this$validityPeriodFlag.equals(other$validityPeriodFlag)) {
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
        Object this$gmtCreate = getGmtCreate();
        Object other$gmtCreate = other.getGmtCreate();
        return this$gmtCreate == null ? other$gmtCreate == null : this$gmtCreate.equals(other$gmtCreate);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncGroupV2Response;
    }

    public int hashCode() {
        Object $groupId = getGroupId();
        int result = (1 * 59) + ($groupId == null ? 43 : $groupId.hashCode());
        Object $groupName = getGroupName();
        int result2 = (result * 59) + ($groupName == null ? 43 : $groupName.hashCode());
        Object $groupAuths = getGroupAuths();
        int result3 = (result2 * 59) + ($groupAuths == null ? 43 : $groupAuths.hashCode());
        Object $allMemberFlag = getAllMemberFlag();
        int result4 = (result3 * 59) + ($allMemberFlag == null ? 43 : $allMemberFlag.hashCode());
        Object $permanentFlag = getPermanentFlag();
        int result5 = (result4 * 59) + ($permanentFlag == null ? 43 : $permanentFlag.hashCode());
        Object $validityPeriodFlag = getValidityPeriodFlag();
        int result6 = (result5 * 59) + ($validityPeriodFlag == null ? 43 : $validityPeriodFlag.hashCode());
        Object $speechContent = getSpeechContent();
        int result7 = (result6 * 59) + ($speechContent == null ? 43 : $speechContent.hashCode());
        Object $showContent = getShowContent();
        int result8 = (result7 * 59) + ($showContent == null ? 43 : $showContent.hashCode());
        Object $gmtCreate = getGmtCreate();
        return (result8 * 59) + ($gmtCreate == null ? 43 : $gmtCreate.hashCode());
    }

    public String toString() {
        return "TerminalSyncGroupV2Response(groupId=" + getGroupId() + ", groupName=" + getGroupName() + ", groupAuths=" + getGroupAuths() + ", allMemberFlag=" + getAllMemberFlag() + ", permanentFlag=" + getPermanentFlag() + ", validityPeriodFlag=" + getValidityPeriodFlag() + ", speechContent=" + getSpeechContent() + ", showContent=" + getShowContent() + ", gmtCreate=" + getGmtCreate() + ")";
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public List<TerminalGroupAuthSyncResponse> getGroupAuths() {
        return this.groupAuths;
    }

    public Integer getAllMemberFlag() {
        return this.allMemberFlag;
    }

    public Integer getPermanentFlag() {
        return this.permanentFlag;
    }

    public Integer getValidityPeriodFlag() {
        return this.validityPeriodFlag;
    }

    public String getSpeechContent() {
        return this.speechContent;
    }

    public String getShowContent() {
        return this.showContent;
    }

    public Date getGmtCreate() {
        return this.gmtCreate;
    }
}
