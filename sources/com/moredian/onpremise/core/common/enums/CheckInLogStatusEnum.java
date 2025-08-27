package com.moredian.onpremise.core.common.enums;

import com.moredian.onpremise.core.common.constants.Constants;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/CheckInLogStatusEnum.class */
public enum CheckInLogStatusEnum {
    ALL(0, Constants.DEVICE_ALL),
    YES(1, "已签到"),
    NO(2, "未签到"),
    BEFORE(3, "提前签到"),
    AFTER(4, "超时签到");

    private int value;
    private String description;

    CheckInLogStatusEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static CheckInLogStatusEnum getByValue(int value) {
        CheckInLogStatusEnum checkInLogStatusEnum = null;
        for (CheckInLogStatusEnum item : values()) {
            if (item.getValue() == value) {
                checkInLogStatusEnum = item;
            }
        }
        return checkInLogStatusEnum;
    }
}
