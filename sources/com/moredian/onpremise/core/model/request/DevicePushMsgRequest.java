package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "推送设备消息请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DevicePushMsgRequest.class */
public class DevicePushMsgRequest extends BaseRequest {
    private static final long serialVersionUID = 8118971626103803206L;

    @ApiModelProperty(name = "allDevice", value = "是否推送所有设备，1-是,0-否，必填")
    private Integer allDevice;

    @ApiModelProperty(name = "deviceSns", value = "设备sn列表，allDevice为1时忽略该项，可为空")
    private List<String> deviceSns;

    @ApiModelProperty(name = "uuid", value = "uuid唯一，用于查询消息推送结果", hidden = true)
    private String uuid;

    @ApiModelProperty(name = "type", value = "消息类型，必填，1-识别开关；13-更改设备激活服务器ip")
    private Integer type;

    @ApiModelProperty(name = "msg", value = "消息文本，json字符串，必填")
    private String msg;

    public void setAllDevice(Integer allDevice) {
        this.allDevice = allDevice;
    }

    public void setDeviceSns(List<String> deviceSns) {
        this.deviceSns = deviceSns;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DevicePushMsgRequest)) {
            return false;
        }
        DevicePushMsgRequest other = (DevicePushMsgRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$allDevice = getAllDevice();
        Object other$allDevice = other.getAllDevice();
        if (this$allDevice == null) {
            if (other$allDevice != null) {
                return false;
            }
        } else if (!this$allDevice.equals(other$allDevice)) {
            return false;
        }
        Object this$deviceSns = getDeviceSns();
        Object other$deviceSns = other.getDeviceSns();
        if (this$deviceSns == null) {
            if (other$deviceSns != null) {
                return false;
            }
        } else if (!this$deviceSns.equals(other$deviceSns)) {
            return false;
        }
        Object this$uuid = getUuid();
        Object other$uuid = other.getUuid();
        if (this$uuid == null) {
            if (other$uuid != null) {
                return false;
            }
        } else if (!this$uuid.equals(other$uuid)) {
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
        Object this$msg = getMsg();
        Object other$msg = other.getMsg();
        return this$msg == null ? other$msg == null : this$msg.equals(other$msg);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DevicePushMsgRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $allDevice = getAllDevice();
        int result = (1 * 59) + ($allDevice == null ? 43 : $allDevice.hashCode());
        Object $deviceSns = getDeviceSns();
        int result2 = (result * 59) + ($deviceSns == null ? 43 : $deviceSns.hashCode());
        Object $uuid = getUuid();
        int result3 = (result2 * 59) + ($uuid == null ? 43 : $uuid.hashCode());
        Object $type = getType();
        int result4 = (result3 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $msg = getMsg();
        return (result4 * 59) + ($msg == null ? 43 : $msg.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DevicePushMsgRequest(allDevice=" + getAllDevice() + ", deviceSns=" + getDeviceSns() + ", uuid=" + getUuid() + ", type=" + getType() + ", msg=" + getMsg() + ")";
    }

    public Integer getAllDevice() {
        return this.allDevice;
    }

    public List<String> getDeviceSns() {
        return this.deviceSns;
    }

    public String getUuid() {
        return this.uuid;
    }

    public Integer getType() {
        return this.type;
    }

    public String getMsg() {
        return this.msg;
    }
}
