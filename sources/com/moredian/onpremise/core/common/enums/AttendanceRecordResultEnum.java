package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/AttendanceRecordResultEnum.class */
public enum AttendanceRecordResultEnum {
    INVALID(0, "无效卡"),
    SUCCESS(1, "打卡成功"),
    LATE(2, "迟到"),
    EARLY(3, "早退"),
    WORK_LACK(4, "上班缺卡"),
    REST_LACK(5, "下班缺卡");

    private int value;
    private String description;

    AttendanceRecordResultEnum(int value, String description) {
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
