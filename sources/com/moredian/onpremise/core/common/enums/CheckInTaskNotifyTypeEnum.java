package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/CheckInTaskNotifyTypeEnum.class */
public enum CheckInTaskNotifyTypeEnum {
    DEPT(1, "部门"),
    MEMBER(2, "成员"),
    DEVICE(3, "设备");

    private int value;
    private String description;

    CheckInTaskNotifyTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static CheckInTaskNotifyTypeEnum getByValue(int value) {
        CheckInTaskNotifyTypeEnum checkInTaskNotifyTypeEnum = null;
        for (CheckInTaskNotifyTypeEnum item : values()) {
            if (item.getValue() == value) {
                checkInTaskNotifyTypeEnum = item;
            }
        }
        return checkInTaskNotifyTypeEnum;
    }
}
