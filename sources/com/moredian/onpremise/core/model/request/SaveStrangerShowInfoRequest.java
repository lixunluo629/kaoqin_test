package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "陌生人刷脸界面文字提示")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveStrangerShowInfoRequest.class */
public class SaveStrangerShowInfoRequest extends BaseRequest {

    @ApiModelProperty(name = "showReminderInfo", value = "陌生人刷脸界面文字提示")
    private String showReminderInfo;

    public void setShowReminderInfo(String showReminderInfo) {
        this.showReminderInfo = showReminderInfo;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveStrangerShowInfoRequest)) {
            return false;
        }
        SaveStrangerShowInfoRequest other = (SaveStrangerShowInfoRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$showReminderInfo = getShowReminderInfo();
        Object other$showReminderInfo = other.getShowReminderInfo();
        return this$showReminderInfo == null ? other$showReminderInfo == null : this$showReminderInfo.equals(other$showReminderInfo);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveStrangerShowInfoRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $showReminderInfo = getShowReminderInfo();
        int result = (1 * 59) + ($showReminderInfo == null ? 43 : $showReminderInfo.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveStrangerShowInfoRequest(showReminderInfo=" + getShowReminderInfo() + ")";
    }

    public String getShowReminderInfo() {
        return this.showReminderInfo;
    }

    public SaveStrangerShowInfoRequest() {
    }

    public SaveStrangerShowInfoRequest(String showReminderInfo) {
        this.showReminderInfo = showReminderInfo;
    }
}
