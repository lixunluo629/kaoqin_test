package io.netty.handler.codec.mqtt;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/mqtt/MqttQoS.class */
public enum MqttQoS {
    AT_MOST_ONCE(0),
    AT_LEAST_ONCE(1),
    EXACTLY_ONCE(2),
    FAILURE(128);

    private final int value;

    MqttQoS(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static MqttQoS valueOf(int value) {
        for (MqttQoS q : values()) {
            if (q.value == value) {
                return q;
            }
        }
        throw new IllegalArgumentException("invalid QoS: " + value);
    }
}
