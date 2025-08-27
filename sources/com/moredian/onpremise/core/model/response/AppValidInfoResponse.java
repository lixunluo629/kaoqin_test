package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "app有效期响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/AppValidInfoResponse.class */
public class AppValidInfoResponse implements Serializable {
    private static final long serialVersionUID = -6743022425392851832L;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_APP_TYPE_KEY, value = "app类型")
    private Integer appType;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_APP_NUM_KEY, value = "数量，返回空表示永久")
    private Integer appNum;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_APP_UNIT_KEY, value = "单位，year|month|day，返回空表示永久")
    private String appUnit;

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public void setAppNum(Integer appNum) {
        this.appNum = appNum;
    }

    public void setAppUnit(String appUnit) {
        this.appUnit = appUnit;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AppValidInfoResponse)) {
            return false;
        }
        AppValidInfoResponse other = (AppValidInfoResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$appType = getAppType();
        Object other$appType = other.getAppType();
        if (this$appType == null) {
            if (other$appType != null) {
                return false;
            }
        } else if (!this$appType.equals(other$appType)) {
            return false;
        }
        Object this$appNum = getAppNum();
        Object other$appNum = other.getAppNum();
        if (this$appNum == null) {
            if (other$appNum != null) {
                return false;
            }
        } else if (!this$appNum.equals(other$appNum)) {
            return false;
        }
        Object this$appUnit = getAppUnit();
        Object other$appUnit = other.getAppUnit();
        return this$appUnit == null ? other$appUnit == null : this$appUnit.equals(other$appUnit);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AppValidInfoResponse;
    }

    public int hashCode() {
        Object $appType = getAppType();
        int result = (1 * 59) + ($appType == null ? 43 : $appType.hashCode());
        Object $appNum = getAppNum();
        int result2 = (result * 59) + ($appNum == null ? 43 : $appNum.hashCode());
        Object $appUnit = getAppUnit();
        return (result2 * 59) + ($appUnit == null ? 43 : $appUnit.hashCode());
    }

    public String toString() {
        return "AppValidInfoResponse(appType=" + getAppType() + ", appNum=" + getAppNum() + ", appUnit=" + getAppUnit() + ")";
    }

    public Integer getAppType() {
        return this.appType;
    }

    public Integer getAppNum() {
        return this.appNum;
    }

    public String getAppUnit() {
        return this.appUnit;
    }
}
