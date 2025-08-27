package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/DeleteOrNotEnum.class */
public enum DeleteOrNotEnum {
    DELETE_YES(1, "删除"),
    DELETE_NO(0, "有效");

    private int value;
    private String description;

    DeleteOrNotEnum(int value, String description) {
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
