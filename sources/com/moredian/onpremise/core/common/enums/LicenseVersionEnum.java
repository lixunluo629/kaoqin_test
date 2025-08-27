package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/LicenseVersionEnum.class */
public enum LicenseVersionEnum {
    V1(1, "v1"),
    V2(2, "v2");

    private int value;
    private String description;

    LicenseVersionEnum(int value, String description) {
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
