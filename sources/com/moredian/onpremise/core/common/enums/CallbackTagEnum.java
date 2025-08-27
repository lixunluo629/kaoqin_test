package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/CallbackTagEnum.class */
public enum CallbackTagEnum {
    REC_SUCCESS("REC_SUCCESS", "识别成功"),
    ONLINE_CHECK("ONLINE_CHECK", "在线鉴权"),
    QR_CHECK("QR_CHECK", "QR鉴权"),
    REC_FAIL("REC_FAIL", "陌生人识别"),
    DEVICE_STATUS_EVENT("DEVICE_STATUS_EVENT", "设备状态事件"),
    WARN_EVENT("WARN_EVENT", "报警事件"),
    TEMPERATURE_RECORD("TEMPERATURE_RECORD", "测温记录");

    private String tag;
    private String description;

    CallbackTagEnum(String tag, String description) {
        this.tag = tag;
        this.description = description;
    }

    public String getTag() {
        return this.tag;
    }

    public String getDescription() {
        return this.description;
    }

    public static CallbackTagEnum getByTag(String tag) {
        CallbackTagEnum callbackTagEnum = null;
        for (CallbackTagEnum item : values()) {
            if (item.getTag().equals(tag)) {
                callbackTagEnum = item;
            }
        }
        return callbackTagEnum;
    }
}
