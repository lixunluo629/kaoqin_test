package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/AllDayFlagEnum.class */
public enum AllDayFlagEnum {
    ALL_DAY(1, "每天"),
    ASSIGN_TIME(0, "指定时间");

    private int value;
    private String description;

    AllDayFlagEnum(int value, String description) {
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
