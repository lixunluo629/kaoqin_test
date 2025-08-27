package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/ViewConfigTypeEnum.class */
public enum ViewConfigTypeEnum {
    LOGIN(1, "登录"),
    APP(2, "app模块"),
    TEXT(3, "文本替换");

    private int value;
    private String description;

    ViewConfigTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static ViewConfigTypeEnum getByValue(int value) {
        ViewConfigTypeEnum viewConfigTypeEnum = null;
        for (ViewConfigTypeEnum item : values()) {
            if (item.getValue() == value) {
                viewConfigTypeEnum = item;
            }
        }
        return viewConfigTypeEnum;
    }
}
