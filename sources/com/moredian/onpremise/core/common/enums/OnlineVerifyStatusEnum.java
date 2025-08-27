package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/OnlineVerifyStatusEnum.class */
public enum OnlineVerifyStatusEnum {
    OPEN_ONLINE_VERIFY(1, "开启"),
    CLOSE_ONLINE_VERIFY(2, "关闭");

    private int value;
    private String description;

    OnlineVerifyStatusEnum(int value, String description) {
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
