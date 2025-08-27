package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/CheckDeviceCodeEnum.class */
public enum CheckDeviceCodeEnum {
    NOT_AVTIVE(1, "未激活"),
    NOT_ALLOW(2, "未授权"),
    NORMAL(0, "正常");

    private int value;
    private String description;

    CheckDeviceCodeEnum(int value, String description) {
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
