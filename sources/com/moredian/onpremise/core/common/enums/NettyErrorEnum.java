package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/NettyErrorEnum.class */
public enum NettyErrorEnum {
    ACTIVE_ERROR(1010, "激活失败"),
    DEVICE_INFO_ERROR(1011, "获取设备信息失败"),
    DEVICE_NOT_ALLOW_ERROR(1012, "设备未授权"),
    DEVICE_NOT_ACTIVE_ERROR(1013, "设备未激活"),
    DEVICE_HEART_ERROR(1014, "设备接入异常");

    private int value;
    private String description;

    NettyErrorEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }
}
