package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/DeviceOnlineStatusEnum.class */
public enum DeviceOnlineStatusEnum {
    ONLINE_YES(1, "在线"),
    ONLINE_NO(2, "离线");

    private int value;
    private String description;

    DeviceOnlineStatusEnum(int value, String description) {
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
