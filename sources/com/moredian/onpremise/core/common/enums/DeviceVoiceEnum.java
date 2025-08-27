package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/DeviceVoiceEnum.class */
public enum DeviceVoiceEnum {
    STANDARD_VOICE(1, "标准模式"),
    BASS_VOICE(2, "低音模式"),
    MUTE_VOICE(3, "静音模式");

    private int value;
    private String description;

    DeviceVoiceEnum(int value, String description) {
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
