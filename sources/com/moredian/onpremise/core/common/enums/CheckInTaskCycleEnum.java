package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/CheckInTaskCycleEnum.class */
public enum CheckInTaskCycleEnum {
    EVERYDAY(1, "每天"),
    SPECIAL(2, "指定日期"),
    SINGLE(3, "单次");

    private int value;
    private String description;

    CheckInTaskCycleEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static CheckInTaskCycleEnum getByValue(int value) {
        CheckInTaskCycleEnum checkInTaskCycleEnum = null;
        for (CheckInTaskCycleEnum item : values()) {
            if (item.getValue() == value) {
                checkInTaskCycleEnum = item;
            }
        }
        return checkInTaskCycleEnum;
    }
}
