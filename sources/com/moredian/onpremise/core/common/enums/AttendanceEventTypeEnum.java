package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/AttendanceEventTypeEnum.class */
public enum AttendanceEventTypeEnum {
    LEAVE(1, "请假"),
    BUSINESS_TRIP(2, "出差"),
    BUSINESS_OUT(3, "外出"),
    OVERTIME(4, "加班"),
    SUPPLEMENT(5, "补卡");

    private int value;
    private String description;

    AttendanceEventTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static AttendanceEventTypeEnum getByValue(int value) {
        AttendanceEventTypeEnum attendanceEventTypeEnum = null;
        for (AttendanceEventTypeEnum item : values()) {
            if (item.getValue() == value) {
                attendanceEventTypeEnum = item;
            }
        }
        return attendanceEventTypeEnum;
    }
}
