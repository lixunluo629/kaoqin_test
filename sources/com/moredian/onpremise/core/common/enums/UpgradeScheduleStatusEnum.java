package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/UpgradeScheduleStatusEnum.class */
public enum UpgradeScheduleStatusEnum {
    WAITING(1, "等待中"),
    UPGRADING(2, "升级中"),
    COMPLETED(3, "已完成"),
    CANCEL(4, "已取消");

    private int value;
    private String description;

    UpgradeScheduleStatusEnum(int value, String description) {
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
