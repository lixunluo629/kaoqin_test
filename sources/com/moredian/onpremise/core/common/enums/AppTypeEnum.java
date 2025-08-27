package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/AppTypeEnum.class */
public enum AppTypeEnum {
    MJ(1, "魔点门禁"),
    QD(2, "魔点签到"),
    KQ(3, "魔点考勤"),
    FK(4, "魔点访客"),
    TC(5, "魔点团餐"),
    CW(6, "魔点测温");

    private int value;
    private String description;

    AppTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static AppTypeEnum getByValue(int value) {
        AppTypeEnum appTypeEnum = null;
        for (AppTypeEnum item : values()) {
            if (item.getValue() == value) {
                appTypeEnum = item;
            }
        }
        return appTypeEnum;
    }
}
