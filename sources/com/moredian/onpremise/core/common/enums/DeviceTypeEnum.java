package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/DeviceTypeEnum.class */
public enum DeviceTypeEnum {
    ENTRANCE_GUARD(1, "门禁机"),
    ENTRANCE_CONSUME_C(2, "消费机-C"),
    ENTRANCE_LIFT(3, "梯控机"),
    ENTRANCE_SELF_CERTIFY(4, "自证机"),
    ENTRANCE_CONSUME_A(5, "消费机-A"),
    ENTRANCE_CONSUME_W(6, "消费机-W"),
    ENTRANCE_COURSE(7, "销课机"),
    ENTRANCE_THERMOMETER_A(8, "温控机-A"),
    ENTRANCE_THERMOMETER_B(9, "温控机-B"),
    ENTRANCE_VISIT(10, "访客机");

    private int value;
    private String description;

    DeviceTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static DeviceTypeEnum getByValue(int value) {
        DeviceTypeEnum deviceTypeEnum = null;
        for (DeviceTypeEnum item : values()) {
            if (item.getValue() == value) {
                deviceTypeEnum = item;
            }
        }
        return deviceTypeEnum;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }
}
