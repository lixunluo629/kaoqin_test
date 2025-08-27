package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "陌生人刷脸语音提示")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveStrangerSpeechInfoRequest.class */
public class SaveStrangerSpeechInfoRequest extends BaseRequest {

    @ApiModelProperty(name = "speechReminderInfo", value = "陌生人刷脸语音提示")
    private String speechReminderInfo;

    public void setSpeechReminderInfo(String speechReminderInfo) {
        this.speechReminderInfo = speechReminderInfo;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveStrangerSpeechInfoRequest)) {
            return false;
        }
        SaveStrangerSpeechInfoRequest other = (SaveStrangerSpeechInfoRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$speechReminderInfo = getSpeechReminderInfo();
        Object other$speechReminderInfo = other.getSpeechReminderInfo();
        return this$speechReminderInfo == null ? other$speechReminderInfo == null : this$speechReminderInfo.equals(other$speechReminderInfo);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveStrangerSpeechInfoRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $speechReminderInfo = getSpeechReminderInfo();
        int result = (1 * 59) + ($speechReminderInfo == null ? 43 : $speechReminderInfo.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveStrangerSpeechInfoRequest(speechReminderInfo=" + getSpeechReminderInfo() + ")";
    }

    public String getSpeechReminderInfo() {
        return this.speechReminderInfo;
    }

    public SaveStrangerSpeechInfoRequest() {
    }

    public SaveStrangerSpeechInfoRequest(String speechReminderInfo) {
        this.speechReminderInfo = speechReminderInfo;
    }
}
