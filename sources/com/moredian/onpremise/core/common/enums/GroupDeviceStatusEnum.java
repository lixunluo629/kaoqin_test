package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/GroupDeviceStatusEnum.class */
public enum GroupDeviceStatusEnum {
    FIRE_STATUS(1, "火警"),
    NEED_UP_STATUS(2, "可升级"),
    OFFLINE_STATUS(3, "离线"),
    UPDATING_STATUS(4, "升级中"),
    NORMAL_STATUS(5, "无状态");

    private int value;
    private String description;

    GroupDeviceStatusEnum(int value, String description) {
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
