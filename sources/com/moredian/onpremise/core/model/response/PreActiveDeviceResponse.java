package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.cache.interceptor.CacheOperationExpressionEvaluator;

@ApiModel(description = "激活设备响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/PreActiveDeviceResponse.class */
public class PreActiveDeviceResponse implements Serializable {
    private static final long serialVersionUID = -815274303672672433L;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = CacheOperationExpressionEvaluator.RESULT_VARIABLE, value = "结果：0失败1成功")
    private Integer result;

    @ApiModelProperty(name = "msg", value = "详情")
    private String msg;

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PreActiveDeviceResponse)) {
            return false;
        }
        PreActiveDeviceResponse other = (PreActiveDeviceResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$deviceName = getDeviceName();
        Object other$deviceName = other.getDeviceName();
        if (this$deviceName == null) {
            if (other$deviceName != null) {
                return false;
            }
        } else if (!this$deviceName.equals(other$deviceName)) {
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
        Object this$result = getResult();
        Object other$result = other.getResult();
        if (this$result == null) {
            if (other$result != null) {
                return false;
            }
        } else if (!this$result.equals(other$result)) {
            return false;
        }
        Object this$msg = getMsg();
        Object other$msg = other.getMsg();
        return this$msg == null ? other$msg == null : this$msg.equals(other$msg);
    }

    protected boolean canEqual(Object other) {
        return other instanceof PreActiveDeviceResponse;
    }

    public int hashCode() {
        Object $deviceName = getDeviceName();
        int result = (1 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $deviceSn = getDeviceSn();
        int result2 = (result * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $result = getResult();
        int result3 = (result2 * 59) + ($result == null ? 43 : $result.hashCode());
        Object $msg = getMsg();
        return (result3 * 59) + ($msg == null ? 43 : $msg.hashCode());
    }

    public String toString() {
        return "PreActiveDeviceResponse(deviceName=" + getDeviceName() + ", deviceSn=" + getDeviceSn() + ", result=" + getResult() + ", msg=" + getMsg() + ")";
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Integer getResult() {
        return this.result;
    }

    public String getMsg() {
        return this.msg;
    }

    public PreActiveDeviceResponse(String deviceName, String deviceSn, Integer result, String msg) {
        this.deviceName = deviceName;
        this.deviceSn = deviceSn;
        this.result = result;
        this.msg = msg;
    }
}
