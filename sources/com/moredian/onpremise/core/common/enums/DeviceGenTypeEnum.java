package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/DeviceGenTypeEnum.class */
public enum DeviceGenTypeEnum {
    BIT_26(1, "26-bit"),
    BIT_34(2, "34-bit");

    private int value;
    private String description;

    DeviceGenTypeEnum(int value, String description) {
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
