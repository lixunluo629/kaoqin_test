package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/DeviceIdentifyModuleEnum.class */
public enum DeviceIdentifyModuleEnum {
    ONE_PERSON(1, "单人识别"),
    MORE_PERSON(2, "多人识别");

    private int value;
    private String description;

    DeviceIdentifyModuleEnum(int value, String description) {
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
