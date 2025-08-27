package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "保存抓拍照模式")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveSnapModeRequest.class */
public class SaveSnapModeRequest extends BaseRequest {
    private static final long serialVersionUID = 5163975648348196097L;

    @ApiModelProperty(name = "mode", value = "抓拍照模式：1-小图；2-大图")
    private Integer mode;

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveSnapModeRequest)) {
            return false;
        }
        SaveSnapModeRequest other = (SaveSnapModeRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$mode = getMode();
        Object other$mode = other.getMode();
        return this$mode == null ? other$mode == null : this$mode.equals(other$mode);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveSnapModeRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $mode = getMode();
        int result = (1 * 59) + ($mode == null ? 43 : $mode.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveSnapModeRequest(mode=" + getMode() + ")";
    }

    public Integer getMode() {
        return this.mode;
    }
}
