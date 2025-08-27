package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/InputFaceChannelEnum.class */
public enum InputFaceChannelEnum {
    WEB(1, "web管理后台"),
    MH2(2, "MH2设备人证采脸");

    private int value;
    private String description;

    InputFaceChannelEnum(int value, String description) {
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
