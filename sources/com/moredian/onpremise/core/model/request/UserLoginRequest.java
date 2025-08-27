package com.moredian.onpremise.core.model.request;

import com.mysql.jdbc.NonRegisteringDriver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "用户登录实体类")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/UserLoginRequest.class */
public class UserLoginRequest implements Serializable {

    @ApiModelProperty(name = "userName", value = "用户名/手机号")
    private String userName;

    @ApiModelProperty(name = NonRegisteringDriver.PASSWORD_PROPERTY_KEY, value = "密码")
    private String password;

    @ApiModelProperty(name = "sessionId", value = "会话id", hidden = true)
    private String sessionId;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserLoginRequest)) {
            return false;
        }
        UserLoginRequest other = (UserLoginRequest) o;
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
        return this$sessionId == null ? other$sessionId == null : this$sessionId.equals(other$sessionId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserLoginRequest;
    }

    public int hashCode() {
        Object $userName = getUserName();
        int result = (1 * 59) + ($userName == null ? 43 : $userName.hashCode());
        Object $password = getPassword();
        int result2 = (result * 59) + ($password == null ? 43 : $password.hashCode());
        Object $sessionId = getSessionId();
        return (result2 * 59) + ($sessionId == null ? 43 : $sessionId.hashCode());
    }

    public String toString() {
        return "UserLoginRequest(userName=" + getUserName() + ", password=" + getPassword() + ", sessionId=" + getSessionId() + ")";
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
}
