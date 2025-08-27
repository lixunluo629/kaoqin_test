package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/VerifyTypeEnum.class */
public enum VerifyTypeEnum {
    FACE(1, "刷脸"),
    CARD(2, "刷卡");

    private int value;
    private String description;

    VerifyTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static VerifyTypeEnum getByValue(int value) {
        VerifyTypeEnum appTypeEnum = null;
        for (VerifyTypeEnum item : values()) {
            if (item.getValue() == value) {
                appTypeEnum = item;
            }
        }
        return appTypeEnum;
    }
}
