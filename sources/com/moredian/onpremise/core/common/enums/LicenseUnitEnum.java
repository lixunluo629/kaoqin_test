package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/LicenseUnitEnum.class */
public enum LicenseUnitEnum {
    YEAR("year", "年"),
    MONTH("month", "月"),
    DAY("day", "日");

    private String key;
    private String description;

    LicenseUnitEnum(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return this.key;
    }

    public String getDescription() {
        return this.description;
    }

    public static LicenseUnitEnum getByKey(String key) {
        LicenseUnitEnum licenseUnitEnum = null;
        for (LicenseUnitEnum item : values()) {
            if (item.getKey().equals(key)) {
                licenseUnitEnum = item;
            }
        }
        return licenseUnitEnum;
    }
}
