package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/WarnRecordTypeEnum.class */
public enum WarnRecordTypeEnum {
    OUT_TIME_OPEN(1, "开门超时报警"),
    ILLEGAL_OPEN(2, "非法开门报警"),
    FIRE(3, "火警"),
    TAKE(4, "防拆报警"),
    PHOTO(5, "红外攻击"),
    BREAK_IN(6, "闯入告警"),
    CONTINUE_IN(7, "连续进场"),
    CONTINUE_OUT(8, "连续出场");

    private int value;
    private String description;

    WarnRecordTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static WarnRecordTypeEnum getByValue(int value) {
        WarnRecordTypeEnum warnRecordTypeEnum = null;
        for (WarnRecordTypeEnum item : values()) {
            if (item.getValue() == value) {
                warnRecordTypeEnum = item;
            }
        }
        return warnRecordTypeEnum;
    }
}
