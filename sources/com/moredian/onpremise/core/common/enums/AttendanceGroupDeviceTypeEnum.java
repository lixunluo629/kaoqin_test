package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/AttendanceGroupDeviceTypeEnum.class */
public enum AttendanceGroupDeviceTypeEnum {
    START_WORK_DEVICE(1, "上班设备"),
    END_WORK_DEVICE(2, "下班设备");

    private int value;
    private String description;

    AttendanceGroupDeviceTypeEnum(int value, String description) {
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
