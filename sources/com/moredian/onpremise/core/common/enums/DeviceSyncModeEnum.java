package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/DeviceSyncModeEnum.class */
public enum DeviceSyncModeEnum {
    SCHEDULE(1, "定时模式"),
    REALTIME(2, "实时模式");

    private Integer value;
    private String description;

    DeviceSyncModeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value.intValue();
    }

    public String getDescription() {
        return this.description;
    }
}
