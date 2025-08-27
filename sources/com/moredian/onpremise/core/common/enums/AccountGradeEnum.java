package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/AccountGradeEnum.class */
public enum AccountGradeEnum {
    SUPER_ACCOUNT(1, "超管"),
    MAIN_ACCOUNT(2, "主管理员"),
    CHILDREN_ACCOUNT(3, "子管理员"),
    NOT_ACCOUNT(4, "非管理员");

    private int value;
    private String description;

    AccountGradeEnum(int value, String description) {
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
