package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/VerifyRecordTypeEnum.class */
public enum VerifyRecordTypeEnum {
    DEVICE_VERIFY(1, "设备端上识别"),
    SERVER_VERIFY(2, "服务端上识别");

    private int value;
    private String description;

    VerifyRecordTypeEnum(int value, String description) {
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
