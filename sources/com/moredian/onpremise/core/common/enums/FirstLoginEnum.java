package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/FirstLoginEnum.class */
public enum FirstLoginEnum {
    HAS_NOT_LOGIN(1, "是"),
    HAS_BEEN_LOGIN(0, "否");

    private int value;
    private String description;

    FirstLoginEnum(int value, String description) {
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
