package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/CommonStatusEnum.class */
public enum CommonStatusEnum {
    YES(1, "是"),
    NO(2, "否");

    private int value;
    private String description;

    CommonStatusEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static CommonStatusEnum getByValue(int value) {
        CommonStatusEnum commonStatusEnum = null;
        for (CommonStatusEnum item : values()) {
            if (item.getValue() == value) {
                commonStatusEnum = item;
            }
        }
        return commonStatusEnum;
    }
}
