package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/DeviceStatusEnum.class */
public enum DeviceStatusEnum {
    ACTIVE_YES(1, "已激活"),
    ACTIVE_NO(2, "未激活");

    private int value;
    private String description;

    DeviceStatusEnum(int value, String description) {
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
