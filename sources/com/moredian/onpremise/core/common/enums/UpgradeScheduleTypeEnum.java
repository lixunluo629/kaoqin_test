package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/UpgradeScheduleTypeEnum.class */
public enum UpgradeScheduleTypeEnum {
    ROM(1, "固件升级"),
    APK(2, "app升级");

    private int value;
    private String description;

    UpgradeScheduleTypeEnum(int value, String description) {
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
