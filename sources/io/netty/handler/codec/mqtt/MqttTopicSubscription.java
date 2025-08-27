package io.netty.handler.codec.mqtt;

import io.netty.util.internal.StringUtil;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/mqtt/MqttTopicSubscription.class */
public final class MqttTopicSubscription {
    private final String topicFilter;
    private final MqttQoS qualityOfService;

    public MqttTopicSubscription(String topicFilter, MqttQoS qualityOfService) {
        this.topicFilter = topicFilter;
        this.qualityOfService = qualityOfService;
    }

    public String topicName() {
        return this.topicFilter;
    }

    public MqttQoS qualityOfService() {
        return this.qualityOfService;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "[topicFilter=" + this.topicFilter + ", qualityOfService=" + this.qualityOfService + ']';
    }
}
