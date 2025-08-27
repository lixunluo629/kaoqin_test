package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/AttendanceGroupTypeEnum.class */
public enum AttendanceGroupTypeEnum {
    FIXED_TYPE(1, "固定排班"),
    MANUAL_TYPE(2, "手动排班"),
    FREE_TYPE(3, "自由打卡");

    private int value;
    private String description;

    AttendanceGroupTypeEnum(int value, String description) {
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
