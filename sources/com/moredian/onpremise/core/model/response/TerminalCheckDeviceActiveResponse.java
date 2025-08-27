package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "设备是否激活查询响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalCheckDeviceActiveResponse.class */
public class TerminalCheckDeviceActiveResponse implements Serializable {

    @ApiModelProperty(name = "activeFlag", value = "true为以激活，false为未激活")
    private Boolean activeFlag;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "token", value = "token令牌")
    private String token;

    @ApiModelProperty(name = "code", value = "状态码", hidden = true)
    private Integer code;

    @ApiModelProperty(name = "systemCurrentTime", value = "当前时间戳")
    private Long systemCurrentTime;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_ORG_CODE_KEY, value = "机构码")
    private String orgCode;

    @ApiModelProperty(name = "timeZone", value = "时区")
    private String timeZone;

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setSystemCurrentTime(Long systemCurrentTime) {
        this.systemCurrentTime = systemCurrentTime;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalCheckDeviceActiveResponse)) {
            return false;
        }
        TerminalCheckDeviceActiveResponse other = (TerminalCheckDeviceActiveResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$activeFlag = getActiveFlag();
        Object other$activeFlag = other.getActiveFlag();
        if (this$activeFlag == null) {
            if (other$activeFlag != null) {
                return false;
            }
        } else if (!this$activeFlag.equals(other$activeFlag)) {
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
        Object this$token = getToken();
        Object other$token = other.getToken();
        if (this$token == null) {
            if (other$token != null) {
                return false;
            }
        } else if (!this$token.equals(other$token)) {
            return false;
        }
        Object this$code = getCode();
        Object other$code = other.getCode();
        if (this$code == null) {
            if (other$code != null) {
                return false;
            }
        } else if (!this$code.equals(other$code)) {
            return false;
        }
        Object this$systemCurrentTime = getSystemCurrentTime();
        Object other$systemCurrentTime = other.getSystemCurrentTime();
        if (this$systemCurrentTime == null) {
            if (other$systemCurrentTime != null) {
                return false;
            }
        } else if (!this$systemCurrentTime.equals(other$systemCurrentTime)) {
            return false;
        }
        Object this$orgCode = getOrgCode();
        Object other$orgCode = other.getOrgCode();
        if (this$orgCode == null) {
            if (other$orgCode != null) {
                return false;
            }
        } else if (!this$orgCode.equals(other$orgCode)) {
            return false;
        }
        Object this$timeZone = getTimeZone();
        Object other$timeZone = other.getTimeZone();
        return this$timeZone == null ? other$timeZone == null : this$timeZone.equals(other$timeZone);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalCheckDeviceActiveResponse;
    }

    public int hashCode() {
        Object $activeFlag = getActiveFlag();
        int result = (1 * 59) + ($activeFlag == null ? 43 : $activeFlag.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $token = getToken();
        int result3 = (result2 * 59) + ($token == null ? 43 : $token.hashCode());
        Object $code = getCode();
        int result4 = (result3 * 59) + ($code == null ? 43 : $code.hashCode());
        Object $systemCurrentTime = getSystemCurrentTime();
        int result5 = (result4 * 59) + ($systemCurrentTime == null ? 43 : $systemCurrentTime.hashCode());
        Object $orgCode = getOrgCode();
        int result6 = (result5 * 59) + ($orgCode == null ? 43 : $orgCode.hashCode());
        Object $timeZone = getTimeZone();
        return (result6 * 59) + ($timeZone == null ? 43 : $timeZone.hashCode());
    }

    public String toString() {
        return "TerminalCheckDeviceActiveResponse(activeFlag=" + getActiveFlag() + ", orgId=" + getOrgId() + ", token=" + getToken() + ", code=" + getCode() + ", systemCurrentTime=" + getSystemCurrentTime() + ", orgCode=" + getOrgCode() + ", timeZone=" + getTimeZone() + ")";
    }

    public Boolean getActiveFlag() {
        return this.activeFlag;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getToken() {
        return this.token;
    }

    public Integer getCode() {
        return this.code;
    }

    public Long getSystemCurrentTime() {
        return this.systemCurrentTime;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public String getTimeZone() {
        return this.timeZone;
    }
}
