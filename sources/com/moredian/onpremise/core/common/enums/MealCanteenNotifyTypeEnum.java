package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/MealCanteenNotifyTypeEnum.class */
public enum MealCanteenNotifyTypeEnum {
    DEPT(1, "部门"),
    MEMBER(2, "成员"),
    DEVICE(3, "设备");

    private int value;
    private String description;

    MealCanteenNotifyTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static MealCanteenNotifyTypeEnum getByValue(int value) {
        MealCanteenNotifyTypeEnum mealCanteenNotifyTypeEnum = null;
        for (MealCanteenNotifyTypeEnum item : values()) {
            if (item.getValue() == value) {
                mealCanteenNotifyTypeEnum = item;
            }
        }
        return mealCanteenNotifyTypeEnum;
    }
}
