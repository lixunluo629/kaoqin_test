package com.moredian.onpremise.core.model.request;

import com.mysql.jdbc.NonRegisteringDriver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "设备登录实体类")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalLoginRequest.class */
public class TerminalLoginRequest implements Serializable {
    private static final long serialVersionUID = -1488111281174470476L;

    @ApiModelProperty(name = "userName", value = "用户名/手机号")
    private String userName;

    @ApiModelProperty(name = NonRegisteringDriver.PASSWORD_PROPERTY_KEY, value = "密码")
    private String password;

    @ApiModelProperty(name = "sessionId", value = "会话id", hidden = true)
    private String sessionId;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalLoginRequest)) {
            return false;
        }
        TerminalLoginRequest other = (TerminalLoginRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$userName = getUserName();
        Object other$userName = other.getUserName();
        if (this$userName == null) {
            if (other$userName != null) {
                return false;
            }
        } else if (!this$userName.equals(other$userName)) {
            return false;
        }
        Object this$password = getPassword();
        Object other$password = other.getPassword();
        if (this$password == null) {
            if (other$password != null) {
                return false;
            }
        } else if (!this$password.equals(other$password)) {
            return false;
        }
        Object this$sessionId = getSessionId();
        Object other$sessionId = other.getSessionId();
        if (this$sessionId == null) {
            if (other$sessionId != null) {
                return false;
            }
        } else if (!this$sessionId.equals(other$sessionId)) {
            return false;
        }
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        return this$deviceSn == null ? other$deviceSn == null : this$deviceSn.equals(other$deviceSn);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalLoginRequest;
    }

    public int hashCode() {
        Object $userName = getUserName();
        int result = (1 * 59) + ($userName == null ? 43 : $userName.hashCode());
        Object $password = getPassword();
        int result2 = (result * 59) + ($password == null ? 43 : $password.hashCode());
        Object $sessionId = getSessionId();
        int result3 = (result2 * 59) + ($sessionId == null ? 43 : $sessionId.hashCode());
        Object $deviceSn = getDeviceSn();
        return (result3 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
    }

    public String toString() {
        return "TerminalLoginRequest(userName=" + getUserName() + ", password=" + getPassword() + ", sessionId=" + getSessionId() + ", deviceSn=" + getDeviceSn() + ")";
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }
}
