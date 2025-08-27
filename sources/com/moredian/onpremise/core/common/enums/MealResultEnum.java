package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/MealResultEnum.class */
public enum MealResultEnum {
    SUCCESS(1, "成功"),
    NO_ACCESS(2, "无就餐权限"),
    EXCESS_CONSUME(3, "超限消费"),
    STRANGER(4, "陌生人"),
    NOT_MEALTIME(5, "非就餐时间");

    private int value;
    private String description;

    MealResultEnum(int value, String description) {
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
