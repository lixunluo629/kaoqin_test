package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/AttendanceRecordTypeEnum.class */
public enum AttendanceRecordTypeEnum {
    BEGAN_WORK(1, "上班"),
    END_WORK(2, "下班");

    private int value;
    private String description;

    AttendanceRecordTypeEnum(int value, String description) {
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
