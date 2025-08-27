package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.aspectj.weaver.model.AsmRelationshipUtils;
import org.springframework.web.servlet.tags.BindTag;

@ApiModel(description = "推送消息结果响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/DevicePushMsgResponse.class */
public class DevicePushMsgResponse implements Serializable {
    private static final long serialVersionUID = 6497088753387419291L;

    @ApiModelProperty(name = "type", value = "消息类型")
    private Integer type;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "msg", value = "消息内容")
    private String msg;

    @ApiModelProperty(name = BindTag.STATUS_VARIABLE_NAME, value = "状态，0-发送失败；1-已发送未响应；2-设备响应成功；3-设备响应败失败")
    private Integer status;

    @ApiModelProperty(name = AsmRelationshipUtils.DECLARE_ERROR, value = "失败原因")
    private String error;

    public void setType(Integer type) {
        this.type = type;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DevicePushMsgResponse)) {
            return false;
        }
        DevicePushMsgResponse other = (DevicePushMsgResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type == null) {
            if (other$type != null) {
                return false;
            }
        } else if (!this$type.equals(other$type)) {
            return false;
        }
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        if (this$deviceSn == null) {
            if (other$deviceSn != null) {
                return false;
            }
        } else if (!this$deviceSn.equals(other$deviceSn)) {
            return false;
        }
        Object this$msg = getMsg();
        Object other$msg = other.getMsg();
        if (this$msg == null) {
            if (other$msg != null) {
                return false;
            }
        } else if (!this$msg.equals(other$msg)) {
            return false;
        }
        Object this$status = getStatus();
        Object other$status = other.getStatus();
        if (this$status == null) {
            if (other$status != null) {
                return false;
            }
        } else if (!this$status.equals(other$status)) {
            return false;
        }
        Object this$error = getError();
        Object other$error = other.getError();
        return this$error == null ? other$error == null : this$error.equals(other$error);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DevicePushMsgResponse;
    }

    public int hashCode() {
        Object $type = getType();
        int result = (1 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $deviceSn = getDeviceSn();
        int result2 = (result * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $msg = getMsg();
        int result3 = (result2 * 59) + ($msg == null ? 43 : $msg.hashCode());
        Object $status = getStatus();
        int result4 = (result3 * 59) + ($status == null ? 43 : $status.hashCode());
        Object $error = getError();
        return (result4 * 59) + ($error == null ? 43 : $error.hashCode());
    }

    public String toString() {
        return "DevicePushMsgResponse(type=" + getType() + ", deviceSn=" + getDeviceSn() + ", msg=" + getMsg() + ", status=" + getStatus() + ", error=" + getError() + ")";
    }

    public Integer getType() {
        return this.type;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getMsg() {
        return this.msg;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getError() {
        return this.error;
    }
}
