package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/CanteenTimeTypeEnum.class */
public enum CanteenTimeTypeEnum {
    BREAKFAST_TIME(1, "早餐"),
    LUNCH_TIME(2, "午餐"),
    DINNER_TIME(3, "晚餐"),
    MIDNIGHT_SNACK_TIME(4, "宵夜");

    private int value;
    private String description;

    CanteenTimeTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static CanteenTimeTypeEnum getByValue(int value) {
        switch (value) {
            case 1:
                return BREAKFAST_TIME;
            case 2:
                return LUNCH_TIME;
            case 3:
                return DINNER_TIME;
            case 4:
                return MIDNIGHT_SNACK_TIME;
            default:
                return null;
        }
    }
}
