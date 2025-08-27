package com.moredian.onpremise.model;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/DeviceUnbindResponse.class */
public class DeviceUnbindResponse extends NettyBaseResponse {
    public static final IOTModelType MODEL_TYPE = IOTModelType.DEVICE_UNBIND_RESPONSE;

    @Override // com.moredian.onpremise.model.NettyBaseResponse
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceUnbindResponse)) {
            return false;
        }
        DeviceUnbindResponse other = (DeviceUnbindResponse) o;
        return other.canEqual(this);
    }

    @Override // com.moredian.onpremise.model.NettyBaseResponse
    protected boolean canEqual(Object other) {
        return other instanceof DeviceUnbindResponse;
    }

    @Override // com.moredian.onpremise.model.NettyBaseResponse
    public int hashCode() {
        return 1;
    }

    @Override // com.moredian.onpremise.model.NettyBaseResponse
    public String toString() {
        return "DeviceUnbindResponse()";
    }
}
