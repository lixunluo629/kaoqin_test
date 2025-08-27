package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/DeviceUpgradeStatusEnum.class */
public enum DeviceUpgradeStatusEnum {
    SUCCESS_STATUS(1, "成功"),
    UPDATING_STATUS(2, "升级中"),
    FAIL_STATUS(3, "失败"),
    WAITING_STATUS(4, "等待升级"),
    CANCEL_STATUS(5, "已取消");

    private int value;
    private String description;

    DeviceUpgradeStatusEnum(int value, String description) {
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
