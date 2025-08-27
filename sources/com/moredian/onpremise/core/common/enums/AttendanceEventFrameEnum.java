package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/AttendanceEventFrameEnum.class */
public enum AttendanceEventFrameEnum {
    AM(1, "上午"),
    PM(2, "下午");

    private int value;
    private String description;

    AttendanceEventFrameEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static AttendanceEventFrameEnum getByValue(int value) {
        AttendanceEventFrameEnum attendanceEventFrameEnum = null;
        for (AttendanceEventFrameEnum item : values()) {
            if (item.getValue() == value) {
                attendanceEventFrameEnum = item;
            }
        }
        return attendanceEventFrameEnum;
    }
}
