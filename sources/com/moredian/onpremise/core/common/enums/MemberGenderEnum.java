package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/MemberGenderEnum.class */
public enum MemberGenderEnum {
    MAN(1, "男"),
    WOMAN(2, "女"),
    UN_KNOW(3, "未选中");

    private int value;
    private String description;

    MemberGenderEnum(int value, String description) {
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
