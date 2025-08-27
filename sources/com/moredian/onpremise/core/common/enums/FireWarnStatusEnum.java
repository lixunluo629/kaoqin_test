package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/FireWarnStatusEnum.class */
public enum FireWarnStatusEnum {
    NORMAL_STATUS(1, "正常"),
    FIRE_STATUS(2, "火警");

    private int value;
    private String description;

    FireWarnStatusEnum(int value, String description) {
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
