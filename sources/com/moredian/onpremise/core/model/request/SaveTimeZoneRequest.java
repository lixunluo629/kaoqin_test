package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "保存时区请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveTimeZoneRequest.class */
public class SaveTimeZoneRequest extends BaseRequest {
    private static final long serialVersionUID = -4520233092780784276L;

    @ApiModelProperty(name = "timeZoneStr", value = "时区字符串，例：GMT+0800")
    private String timeZoneStr;

    public void setTimeZoneStr(String timeZoneStr) {
        this.timeZoneStr = timeZoneStr;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveTimeZoneRequest)) {
            return false;
        }
        SaveTimeZoneRequest other = (SaveTimeZoneRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$timeZoneStr = getTimeZoneStr();
        Object other$timeZoneStr = other.getTimeZoneStr();
        return this$timeZoneStr == null ? other$timeZoneStr == null : this$timeZoneStr.equals(other$timeZoneStr);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveTimeZoneRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $timeZoneStr = getTimeZoneStr();
        int result = (1 * 59) + ($timeZoneStr == null ? 43 : $timeZoneStr.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveTimeZoneRequest(timeZoneStr=" + getTimeZoneStr() + ")";
    }

    public String getTimeZoneStr() {
        return this.timeZoneStr;
    }
}
