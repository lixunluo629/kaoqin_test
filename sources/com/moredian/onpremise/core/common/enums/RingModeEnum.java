package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/RingModeEnum.class */
public enum RingModeEnum {
    CRYSTAL(1, "水晶"),
    SILK(2, "丝绸");

    private int value;
    private String description;

    RingModeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static RingModeEnum getByValue(int value) {
        RingModeEnum ringModeEnum = null;
        for (RingModeEnum item : values()) {
            if (item.getValue() == value) {
                ringModeEnum = item;
            }
        }
        return ringModeEnum;
    }
}
