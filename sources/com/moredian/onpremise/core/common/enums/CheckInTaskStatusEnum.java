package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/CheckInTaskStatusEnum.class */
public enum CheckInTaskStatusEnum {
    YES(1, "开启"),
    NO(2, "关闭");

    private int value;
    private String description;

    CheckInTaskStatusEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static CheckInTaskStatusEnum getByValue(int value) {
        CheckInTaskStatusEnum checkInTaskStatusEnum = null;
        for (CheckInTaskStatusEnum item : values()) {
            if (item.getValue() == value) {
                checkInTaskStatusEnum = item;
            }
        }
        return checkInTaskStatusEnum;
    }
}
