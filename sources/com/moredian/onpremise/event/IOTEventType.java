package com.moredian.onpremise.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.tomcat.jni.Status;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/event/IOTEventType.class */
public enum IOTEventType {
    HELLO_WORLD(0),
    HEARTBEAT(1),
    REDIRECT_DEVICE(2),
    REJECT_DEVICE(3),
    ACK_FROM_DEVICE(4),
    DEVICE_UPGRADE(5),
    DEVICE_ALARM(6),
    DEVICE_UPGRADE_PROGRESS(7),
    DEVICE_STATE(8),
    MODEL_TRANSFER(9),
    DEVICE_ROM_UPDATE(10),
    DEVICE_ROM_UPDATE_STATUS(11),
    DEVICE_APP_UPDATE(12),
    DEVICE_APP_UPDATE_STATUS(13),
    DEVICE_DISCONNECT(10000),
    REBOOT_DEVICE(Status.APR_ENOSTAT);

    private static final Map<Integer, IOTEventType> MAP = new ConcurrentHashMap();
    private final int type;

    static {
        for (IOTEventType type : values()) {
            MAP.put(Integer.valueOf(type.getType()), type);
        }
    }

    IOTEventType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public static IOTEventType fromType(int type) {
        IOTEventType ret = MAP.get(Integer.valueOf(type));
        if (ret == null) {
            throw new IllegalArgumentException("Unknown IOT Event type: " + type);
        }
        return ret;
    }

    public boolean equals(Integer type) {
        return type != null && this.type == type.intValue();
    }

    public boolean equals(int type) {
        return this.type == type;
    }
}
