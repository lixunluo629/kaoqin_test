package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/SnapModeEnum.class */
public enum SnapModeEnum {
    SMALL(1, "小图"),
    BIG(2, "大图");

    private int value;
    private String description;

    SnapModeEnum(int value, String description) {
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
