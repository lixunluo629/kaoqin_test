package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "陌生人刷脸提示信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveStrangerInfoRequest.class */
public class SaveStrangerInfoRequest extends BaseRequest {
    private static final long serialVersionUID = -6944889747883329102L;

    @ApiModelProperty(name = "frame", value = "判定陌生人帧数")
    private Integer frame;

    @ApiModelProperty(name = "showReminderInfo", value = "陌生人刷脸界面文字提示")
    private String showReminderInfo;

    @ApiModelProperty(name = "speechReminderInfo", value = "陌生人刷脸语音提示")
    private String speechReminderInfo;

    public void setFrame(Integer frame) {
        this.frame = frame;
    }

    public void setShowReminderInfo(String showReminderInfo) {
        this.showReminderInfo = showReminderInfo;
    }

    public void setSpeechReminderInfo(String speechReminderInfo) {
        this.speechReminderInfo = speechReminderInfo;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveStrangerInfoRequest)) {
            return false;
        }
        SaveStrangerInfoRequest other = (SaveStrangerInfoRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$frame = getFrame();
        Object other$frame = other.getFrame();
        if (this$frame == null) {
            if (other$frame != null) {
                return false;
            }
        } else if (!this$frame.equals(other$frame)) {
            return false;
        }
        Object this$showReminderInfo = getShowReminderInfo();
        Object other$showReminderInfo = other.getShowReminderInfo();
        if (this$showReminderInfo == null) {
            if (other$showReminderInfo != null) {
                return false;
            }
        } else if (!this$showReminderInfo.equals(other$showReminderInfo)) {
            return false;
        }
        Object this$speechReminderInfo = getSpeechReminderInfo();
        Object other$speechReminderInfo = other.getSpeechReminderInfo();
        return this$speechReminderInfo == null ? other$speechReminderInfo == null : this$speechReminderInfo.equals(other$speechReminderInfo);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveStrangerInfoRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $frame = getFrame();
        int result = (1 * 59) + ($frame == null ? 43 : $frame.hashCode());
        Object $showReminderInfo = getShowReminderInfo();
        int result2 = (result * 59) + ($showReminderInfo == null ? 43 : $showReminderInfo.hashCode());
        Object $speechReminderInfo = getSpeechReminderInfo();
        return (result2 * 59) + ($speechReminderInfo == null ? 43 : $speechReminderInfo.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveStrangerInfoRequest(frame=" + getFrame() + ", showReminderInfo=" + getShowReminderInfo() + ", speechReminderInfo=" + getSpeechReminderInfo() + ")";
    }

    public Integer getFrame() {
        return this.frame;
    }

    public String getShowReminderInfo() {
        return this.showReminderInfo;
    }

    public String getSpeechReminderInfo() {
        return this.speechReminderInfo;
    }
}
