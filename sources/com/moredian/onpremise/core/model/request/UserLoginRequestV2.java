package com.moredian.onpremise.core.model.request;

import com.mysql.jdbc.NonRegisteringDriver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "用户登录实体类")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/UserLoginRequestV2.class */
public class UserLoginRequestV2 implements Serializable {
    private static final long serialVersionUID = -633561978420100201L;

    @ApiModelProperty(name = "userName", value = "用户名/手机号")
    private String userName;

    @ApiModelProperty(name = NonRegisteringDriver.PASSWORD_PROPERTY_KEY, value = "密码，计算规则：md5(md5(输入密码)+当前时间戳)")
    private String password;

    @ApiModelProperty(name = "timestamp", value = "当前时间戳")
    private Long timestamp;

    @ApiModelProperty(name = "sessionId", value = "会话id", hidden = true)
    private String sessionId;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserLoginRequestV2)) {
            return false;
        }
        UserLoginRequestV2 other = (UserLoginRequestV2) o;
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
        Object this$timestamp = getTimestamp();
        Object other$timestamp = other.getTimestamp();
        if (this$timestamp == null) {
            if (other$timestamp != null) {
                return false;
            }
        } else if (!this$timestamp.equals(other$timestamp)) {
            return false;
        }
        Object this$sessionId = getSessionId();
        Object other$sessionId = other.getSessionId();
        return this$sessionId == null ? other$sessionId == null : this$sessionId.equals(other$sessionId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserLoginRequestV2;
    }

    public int hashCode() {
        Object $userName = getUserName();
        int result = (1 * 59) + ($userName == null ? 43 : $userName.hashCode());
        Object $password = getPassword();
        int result2 = (result * 59) + ($password == null ? 43 : $password.hashCode());
        Object $timestamp = getTimestamp();
        int result3 = (result2 * 59) + ($timestamp == null ? 43 : $timestamp.hashCode());
        Object $sessionId = getSessionId();
        return (result3 * 59) + ($sessionId == null ? 43 : $sessionId.hashCode());
    }

    public String toString() {
        return "UserLoginRequestV2(userName=" + getUserName() + ", password=" + getPassword() + ", timestamp=" + getTimestamp() + ", sessionId=" + getSessionId() + ")";
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public String getSessionId() {
        return this.sessionId;
    }
}
