package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/MemberTypeEnum.class */
public enum MemberTypeEnum {
    DEPT(1, "部门"),
    MEMBER(2, "成员");

    private int value;
    private String description;

    MemberTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static MemberTypeEnum getByValue(int value) {
        switch (value) {
            case 1:
                return DEPT;
            case 2:
                return MEMBER;
            default:
                return null;
        }
    }
}
