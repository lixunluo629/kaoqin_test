package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/DeviceIdentifyDistanceEnum.class */
public enum DeviceIdentifyDistanceEnum {
    ONE(1, "1m"),
    TWO(2, "2m"),
    THREE(3, "3m"),
    FOUR(4, "4m"),
    FIVE(5, "5m"),
    SIX(6, "6m"),
    SEVEN(7, "7m"),
    EIGHT(8, "8m"),
    NINE(9, "9m"),
    TEN(10, "10m");

    private int value;
    private String description;

    DeviceIdentifyDistanceEnum(int value, String description) {
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
