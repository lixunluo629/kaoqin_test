package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/DeviceStatusEventEnum.class */
public enum DeviceStatusEventEnum {
    ACTIVE(1, "激活"),
    UNBIND(2, "解绑"),
    ONLINE(3, "上线"),
    OFFLINE(4, "下线");

    private String description;
    private int status = this.status;
    private int status = this.status;

    DeviceStatusEventEnum(int tag, String description) {
        this.description = description;
    }

    public int getStatus() {
        return this.status;
    }

    public String getDescription() {
        return this.description;
    }

    public static DeviceStatusEventEnum getByStatus(int status) {
        DeviceStatusEventEnum deviceStatusEventEnum = null;
        for (DeviceStatusEventEnum item : values()) {
            if (item.getStatus() == status) {
                deviceStatusEventEnum = item;
            }
        }
        return deviceStatusEventEnum;
    }
}
