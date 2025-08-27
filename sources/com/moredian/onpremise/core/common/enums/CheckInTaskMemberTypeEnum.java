package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/CheckInTaskMemberTypeEnum.class */
public enum CheckInTaskMemberTypeEnum {
    DEPT(1, "部门"),
    MEMBER(2, "成员");

    private int value;
    private String description;

    CheckInTaskMemberTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static CheckInTaskMemberTypeEnum getByValue(int value) {
        CheckInTaskMemberTypeEnum commonStatusEnum = null;
        for (CheckInTaskMemberTypeEnum item : values()) {
            if (item.getValue() == value) {
                commonStatusEnum = item;
            }
        }
        return commonStatusEnum;
    }
}
