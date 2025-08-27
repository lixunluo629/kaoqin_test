package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/AttendanceSupplementTypeEnum.class */
public enum AttendanceSupplementTypeEnum {
    WORK(1, "上班卡"),
    OFF_WORK(2, "下班卡");

    private int value;
    private String description;

    AttendanceSupplementTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static AttendanceSupplementTypeEnum getByValue(int value) {
        AttendanceSupplementTypeEnum attendanceSupplementTypeEnum = null;
        for (AttendanceSupplementTypeEnum item : values()) {
            if (item.getValue() == value) {
                attendanceSupplementTypeEnum = item;
            }
        }
        return attendanceSupplementTypeEnum;
    }
}
