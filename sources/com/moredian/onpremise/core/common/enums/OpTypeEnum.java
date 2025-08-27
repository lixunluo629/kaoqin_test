package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/OpTypeEnum.class */
public enum OpTypeEnum {
    ADD(1, "增加"),
    REMOVE(2, "删除"),
    MODIFY(3, "修改");

    private int value;
    private String description;

    OpTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static OpTypeEnum getByValue(int value) {
        OpTypeEnum opTypeEnum = null;
        for (OpTypeEnum item : values()) {
            if (item.getValue() == value) {
                opTypeEnum = item;
            }
        }
        return opTypeEnum;
    }
}
