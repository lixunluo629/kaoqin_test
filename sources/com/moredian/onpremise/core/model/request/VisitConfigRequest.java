package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.model.dto.PassTimeDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "访客设置参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/VisitConfigRequest.class */
public class VisitConfigRequest extends BaseRequest {
    private static final long serialVersionUID = 3752927476150293301L;

    @ApiModelProperty(name = "id", value = "更新时必填")
    private Long id;

    @ApiModelProperty(name = "wallpaperUrl", value = "壁纸url")
    private String wallpaperUrl;

    @ApiModelProperty(name = "screensaverFlag", value = "屏保是否开启，1-开；0-关，必填")
    private Integer screensaverFlag;

    @ApiModelProperty(name = "updateScreensaver", value = "屏保是否需更新，1-是；0-否，必填")
    private Integer updateScreensaver;

    @ApiModelProperty(name = "screensaverUrl", value = "屏保url")
    private List<String> screensaverUrl;

    @ApiModelProperty(name = "idCardVerify", value = "开启人证合验，1-开；0-关，必填")
    private Integer idCardVerify;

    @ApiModelProperty(name = "inputFace", value = "开启人证采脸，1-开；0-关，必填")
    private Integer inputFace;

    @ApiModelProperty(name = "intervieweeFlag", value = "是否填写被访者信息，1-是；0-否，必填")
    private Integer intervieweeFlag;

    @ApiModelProperty(name = "allDayFlag", value = "是否全天，1-是；0-否，必填")
    private Integer allDayFlag;

    @ApiModelProperty(name = "passTimeList", value = "权限允许通行时间段，例[{\"startTime\":\"09:21\",\"endTime\":\"10:11\"}]")
    private List<PassTimeDto> passTimeList;

    @ApiModelProperty(name = "cycleFlag", value = "重复每天，1-是；0-否，必填")
    private Integer cycleFlag;

    @ApiModelProperty(name = "scope", value = "权限重复周期。例：[1,2]")
    private List<Integer> scope;

    @ApiModelProperty(name = "showContent", value = "界面提示文本内容")
    private String showContent;

    @ApiModelProperty(name = "speechContent", value = "语音播报文本内容")
    private String speechContent;

    public void setId(Long id) {
        this.id = id;
    }

    public void setWallpaperUrl(String wallpaperUrl) {
        this.wallpaperUrl = wallpaperUrl;
    }

    public void setScreensaverFlag(Integer screensaverFlag) {
        this.screensaverFlag = screensaverFlag;
    }

    public void setUpdateScreensaver(Integer updateScreensaver) {
        this.updateScreensaver = updateScreensaver;
    }

    public void setScreensaverUrl(List<String> screensaverUrl) {
        this.screensaverUrl = screensaverUrl;
    }

    public void setIdCardVerify(Integer idCardVerify) {
        this.idCardVerify = idCardVerify;
    }

    public void setInputFace(Integer inputFace) {
        this.inputFace = inputFace;
    }

    public void setIntervieweeFlag(Integer intervieweeFlag) {
        this.intervieweeFlag = intervieweeFlag;
    }

    public void setAllDayFlag(Integer allDayFlag) {
        this.allDayFlag = allDayFlag;
    }

    public void setPassTimeList(List<PassTimeDto> passTimeList) {
        this.passTimeList = passTimeList;
    }

    public void setCycleFlag(Integer cycleFlag) {
        this.cycleFlag = cycleFlag;
    }

    public void setScope(List<Integer> scope) {
        this.scope = scope;
    }

    public void setShowContent(String showContent) {
        this.showContent = showContent;
    }

    public void setSpeechContent(String speechContent) {
        this.speechContent = speechContent;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VisitConfigRequest)) {
            return false;
        }
        VisitConfigRequest other = (VisitConfigRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        if (this$id == null) {
            if (other$id != null) {
                return false;
            }
        } else if (!this$id.equals(other$id)) {
            return false;
        }
        Object this$wallpaperUrl = getWallpaperUrl();
        Object other$wallpaperUrl = other.getWallpaperUrl();
        if (this$wallpaperUrl == null) {
            if (other$wallpaperUrl != null) {
                return false;
            }
        } else if (!this$wallpaperUrl.equals(other$wallpaperUrl)) {
            return false;
        }
        Object this$screensaverFlag = getScreensaverFlag();
        Object other$screensaverFlag = other.getScreensaverFlag();
        if (this$screensaverFlag == null) {
            if (other$screensaverFlag != null) {
                return false;
            }
        } else if (!this$screensaverFlag.equals(other$screensaverFlag)) {
            return false;
        }
        Object this$updateScreensaver = getUpdateScreensaver();
        Object other$updateScreensaver = other.getUpdateScreensaver();
        if (this$updateScreensaver == null) {
            if (other$updateScreensaver != null) {
                return false;
            }
        } else if (!this$updateScreensaver.equals(other$updateScreensaver)) {
            return false;
        }
        Object this$screensaverUrl = getScreensaverUrl();
        Object other$screensaverUrl = other.getScreensaverUrl();
        if (this$screensaverUrl == null) {
            if (other$screensaverUrl != null) {
                return false;
            }
        } else if (!this$screensaverUrl.equals(other$screensaverUrl)) {
            return false;
        }
        Object this$idCardVerify = getIdCardVerify();
        Object other$idCardVerify = other.getIdCardVerify();
        if (this$idCardVerify == null) {
            if (other$idCardVerify != null) {
                return false;
            }
        } else if (!this$idCardVerify.equals(other$idCardVerify)) {
            return false;
        }
        Object this$inputFace = getInputFace();
        Object other$inputFace = other.getInputFace();
        if (this$inputFace == null) {
            if (other$inputFace != null) {
                return false;
            }
        } else if (!this$inputFace.equals(other$inputFace)) {
            return false;
        }
        Object this$intervieweeFlag = getIntervieweeFlag();
        Object other$intervieweeFlag = other.getIntervieweeFlag();
        if (this$intervieweeFlag == null) {
            if (other$intervieweeFlag != null) {
                return false;
            }
        } else if (!this$intervieweeFlag.equals(other$intervieweeFlag)) {
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
        Object this$passTimeList = getPassTimeList();
        Object other$passTimeList = other.getPassTimeList();
        if (this$passTimeList == null) {
            if (other$passTimeList != null) {
                return false;
            }
        } else if (!this$passTimeList.equals(other$passTimeList)) {
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
        Object this$scope = getScope();
        Object other$scope = other.getScope();
        if (this$scope == null) {
            if (other$scope != null) {
                return false;
            }
        } else if (!this$scope.equals(other$scope)) {
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
        return this$speechContent == null ? other$speechContent == null : this$speechContent.equals(other$speechContent);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof VisitConfigRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $wallpaperUrl = getWallpaperUrl();
        int result2 = (result * 59) + ($wallpaperUrl == null ? 43 : $wallpaperUrl.hashCode());
        Object $screensaverFlag = getScreensaverFlag();
        int result3 = (result2 * 59) + ($screensaverFlag == null ? 43 : $screensaverFlag.hashCode());
        Object $updateScreensaver = getUpdateScreensaver();
        int result4 = (result3 * 59) + ($updateScreensaver == null ? 43 : $updateScreensaver.hashCode());
        Object $screensaverUrl = getScreensaverUrl();
        int result5 = (result4 * 59) + ($screensaverUrl == null ? 43 : $screensaverUrl.hashCode());
        Object $idCardVerify = getIdCardVerify();
        int result6 = (result5 * 59) + ($idCardVerify == null ? 43 : $idCardVerify.hashCode());
        Object $inputFace = getInputFace();
        int result7 = (result6 * 59) + ($inputFace == null ? 43 : $inputFace.hashCode());
        Object $intervieweeFlag = getIntervieweeFlag();
        int result8 = (result7 * 59) + ($intervieweeFlag == null ? 43 : $intervieweeFlag.hashCode());
        Object $allDayFlag = getAllDayFlag();
        int result9 = (result8 * 59) + ($allDayFlag == null ? 43 : $allDayFlag.hashCode());
        Object $passTimeList = getPassTimeList();
        int result10 = (result9 * 59) + ($passTimeList == null ? 43 : $passTimeList.hashCode());
        Object $cycleFlag = getCycleFlag();
        int result11 = (result10 * 59) + ($cycleFlag == null ? 43 : $cycleFlag.hashCode());
        Object $scope = getScope();
        int result12 = (result11 * 59) + ($scope == null ? 43 : $scope.hashCode());
        Object $showContent = getShowContent();
        int result13 = (result12 * 59) + ($showContent == null ? 43 : $showContent.hashCode());
        Object $speechContent = getSpeechContent();
        return (result13 * 59) + ($speechContent == null ? 43 : $speechContent.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "VisitConfigRequest(id=" + getId() + ", wallpaperUrl=" + getWallpaperUrl() + ", screensaverFlag=" + getScreensaverFlag() + ", updateScreensaver=" + getUpdateScreensaver() + ", screensaverUrl=" + getScreensaverUrl() + ", idCardVerify=" + getIdCardVerify() + ", inputFace=" + getInputFace() + ", intervieweeFlag=" + getIntervieweeFlag() + ", allDayFlag=" + getAllDayFlag() + ", passTimeList=" + getPassTimeList() + ", cycleFlag=" + getCycleFlag() + ", scope=" + getScope() + ", showContent=" + getShowContent() + ", speechContent=" + getSpeechContent() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public String getWallpaperUrl() {
        return this.wallpaperUrl;
    }

    public Integer getScreensaverFlag() {
        return this.screensaverFlag;
    }

    public Integer getUpdateScreensaver() {
        return this.updateScreensaver;
    }

    public List<String> getScreensaverUrl() {
        return this.screensaverUrl;
    }

    public Integer getIdCardVerify() {
        return this.idCardVerify;
    }

    public Integer getInputFace() {
        return this.inputFace;
    }

    public Integer getIntervieweeFlag() {
        return this.intervieweeFlag;
    }

    public Integer getAllDayFlag() {
        return this.allDayFlag;
    }

    public List<PassTimeDto> getPassTimeList() {
        return this.passTimeList;
    }

    public Integer getCycleFlag() {
        return this.cycleFlag;
    }

    public List<Integer> getScope() {
        return this.scope;
    }

    public String getShowContent() {
        return this.showContent;
    }

    public String getSpeechContent() {
        return this.speechContent;
    }
}
