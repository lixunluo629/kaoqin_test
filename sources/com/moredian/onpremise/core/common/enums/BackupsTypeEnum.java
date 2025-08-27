package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/BackupsTypeEnum.class */
public enum BackupsTypeEnum {
    MANUAL_BACKUPS(1, "手动备份"),
    AUTOMATIC_BACKUPS(2, "自动备份");

    private int value;
    private String description;

    BackupsTypeEnum(int value, String description) {
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
