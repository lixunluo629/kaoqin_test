package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/AttendanceLeaveTypeEnum.class */
public enum AttendanceLeaveTypeEnum {
    ANNUL(1, "年假"),
    SICK(2, "病假"),
    LEAVE(3, "事假"),
    MARRIAGE(4, "婚假"),
    FUNERAL(5, "丧假"),
    MATERNITY(6, "产假"),
    PATERNITY(7, "陪产假"),
    CHANGE(8, "调休"),
    ELSE(9, "其他");

    private int value;
    private String description;

    AttendanceLeaveTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static AttendanceLeaveTypeEnum getByValue(int value) {
        AttendanceLeaveTypeEnum attendanceLeaveTypeEnum = null;
        for (AttendanceLeaveTypeEnum item : values()) {
            if (item.getValue() == value) {
                attendanceLeaveTypeEnum = item;
            }
        }
        return attendanceLeaveTypeEnum;
    }
}
