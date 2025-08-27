package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/AttendanceStatusTypeEnum.class */
public enum AttendanceStatusTypeEnum {
    ATTENDANCE_BY_SELF(1, "用户打卡"),
    ATTENDANCE_BY_SIPPLEMENT(2, "补卡");

    private int value;
    private String description;

    AttendanceStatusTypeEnum(int value, String description) {
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
