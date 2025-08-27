package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/MemberCardStatusEnum.class */
public enum MemberCardStatusEnum {
    USED(1, "使用中"),
    DISABLE(2, "已禁用"),
    INVALID(3, "无效卡");

    private int value;
    private String description;

    MemberCardStatusEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static MemberCardStatusEnum getByValue(int value) {
        MemberCardStatusEnum appTypeEnum = null;
        for (MemberCardStatusEnum item : values()) {
            if (item.getValue() == value) {
                appTypeEnum = item;
            }
        }
        return appTypeEnum;
    }
}
