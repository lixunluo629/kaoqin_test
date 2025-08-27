package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "保存应用")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveAppRequest.class */
public class SaveAppRequest extends BaseRequest {

    @ApiModelProperty(name = "appId", value = "应用id")
    private Long appId;

    @ApiModelProperty(name = "appName", value = "应用名称")
    private String appName;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_APP_TYPE_KEY, value = "应用类型：1-魔点门禁")
    private String appType;

    @ApiModelProperty(name = "appCode", value = "应用编码", hidden = true)
    private String appCode;

    @ApiModelProperty(name = "appIcon", value = "应用图标", hidden = true)
    private String appIcon;

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveAppRequest)) {
            return false;
        }
        SaveAppRequest other = (SaveAppRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$appId = getAppId();
        Object other$appId = other.getAppId();
        if (this$appId == null) {
            if (other$appId != null) {
                return false;
            }
        } else if (!this$appId.equals(other$appId)) {
            return false;
        }
        Object this$appName = getAppName();
        Object other$appName = other.getAppName();
        if (this$appName == null) {
            if (other$appName != null) {
                return false;
            }
        } else if (!this$appName.equals(other$appName)) {
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
        Object this$appCode = getAppCode();
        Object other$appCode = other.getAppCode();
        if (this$appCode == null) {
            if (other$appCode != null) {
                return false;
            }
        } else if (!this$appCode.equals(other$appCode)) {
            return false;
        }
        Object this$appIcon = getAppIcon();
        Object other$appIcon = other.getAppIcon();
        return this$appIcon == null ? other$appIcon == null : this$appIcon.equals(other$appIcon);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveAppRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $appId = getAppId();
        int result = (1 * 59) + ($appId == null ? 43 : $appId.hashCode());
        Object $appName = getAppName();
        int result2 = (result * 59) + ($appName == null ? 43 : $appName.hashCode());
        Object $appType = getAppType();
        int result3 = (result2 * 59) + ($appType == null ? 43 : $appType.hashCode());
        Object $appCode = getAppCode();
        int result4 = (result3 * 59) + ($appCode == null ? 43 : $appCode.hashCode());
        Object $appIcon = getAppIcon();
        return (result4 * 59) + ($appIcon == null ? 43 : $appIcon.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveAppRequest(appId=" + getAppId() + ", appName=" + getAppName() + ", appType=" + getAppType() + ", appCode=" + getAppCode() + ", appIcon=" + getAppIcon() + ")";
    }

    public Long getAppId() {
        return this.appId;
    }

    public String getAppName() {
        return this.appName;
    }

    public String getAppType() {
        return this.appType;
    }

    public String getAppCode() {
        return this.appCode;
    }

    public String getAppIcon() {
        return this.appIcon;
    }
}
