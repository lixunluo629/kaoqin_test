package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/MealCardStatusEnum.class */
public enum MealCardStatusEnum {
    USING(1, "使用中"),
    DISABLE(2, "已禁用"),
    INVALID(3, "无效卡"),
    NOT_BOUND(4, "未绑卡");

    private int value;
    private String description;

    MealCardStatusEnum(int value, String description) {
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
