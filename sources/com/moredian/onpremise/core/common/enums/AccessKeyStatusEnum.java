package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/AccessKeyStatusEnum.class */
public enum AccessKeyStatusEnum {
    WAIT_ACTIVE(1, "待激活"),
    HAS_USED(2, "使用中"),
    HAS_EXPIRED(3, "已过期");

    private int value;
    private String description;

    AccessKeyStatusEnum(int value, String description) {
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
