package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "工作台应用信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/AppResponse.class */
public class AppResponse implements Serializable {

    @ApiModelProperty(name = "appId", value = "应用id")
    private Long appId;

    @ApiModelProperty(name = "orgId", value = "机构id", hidden = true)
    private Long orgId;

    @ApiModelProperty(name = "appName", value = "应用名称")
    private String appName;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_APP_TYPE_KEY, value = "应用类型：1-魔点门禁")
    private Integer appType;

    @ApiModelProperty(name = "appCode", value = "应用编码", hidden = true)
    private String appCode;

    @ApiModelProperty(name = "appIcon", value = "应用图标", hidden = true)
    private String appIcon;

    @ApiModelProperty(name = "appValid", value = "应用有效期")
    private Date appValid;

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public void setAppValid(Date appValid) {
        this.appValid = appValid;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AppResponse)) {
            return false;
        }
        AppResponse other = (AppResponse) o;
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
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        if (this$orgId == null) {
            if (other$orgId != null) {
                return false;
            }
        } else if (!this$orgId.equals(other$orgId)) {
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
        if (this$appIcon == null) {
            if (other$appIcon != null) {
                return false;
            }
        } else if (!this$appIcon.equals(other$appIcon)) {
            return false;
        }
        Object this$appValid = getAppValid();
        Object other$appValid = other.getAppValid();
        return this$appValid == null ? other$appValid == null : this$appValid.equals(other$appValid);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AppResponse;
    }

    public int hashCode() {
        Object $appId = getAppId();
        int result = (1 * 59) + ($appId == null ? 43 : $appId.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $appName = getAppName();
        int result3 = (result2 * 59) + ($appName == null ? 43 : $appName.hashCode());
        Object $appType = getAppType();
        int result4 = (result3 * 59) + ($appType == null ? 43 : $appType.hashCode());
        Object $appCode = getAppCode();
        int result5 = (result4 * 59) + ($appCode == null ? 43 : $appCode.hashCode());
        Object $appIcon = getAppIcon();
        int result6 = (result5 * 59) + ($appIcon == null ? 43 : $appIcon.hashCode());
        Object $appValid = getAppValid();
        return (result6 * 59) + ($appValid == null ? 43 : $appValid.hashCode());
    }

    public String toString() {
        return "AppResponse(appId=" + getAppId() + ", orgId=" + getOrgId() + ", appName=" + getAppName() + ", appType=" + getAppType() + ", appCode=" + getAppCode() + ", appIcon=" + getAppIcon() + ", appValid=" + getAppValid() + ")";
    }

    public Long getAppId() {
        return this.appId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getAppName() {
        return this.appName;
    }

    public Integer getAppType() {
        return this.appType;
    }

    public String getAppCode() {
        return this.appCode;
    }

    public String getAppIcon() {
        return this.appIcon;
    }

    public Date getAppValid() {
        return this.appValid;
    }
}
