package io.netty.handler.codec.mqtt;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/mqtt/MqttSubAckMessage.class */
public final class MqttSubAckMessage extends MqttMessage {
    public MqttSubAckMessage(MqttFixedHeader mqttFixedHeader, MqttMessageIdVariableHeader variableHeader, MqttSubAckPayload payload) {
        super(mqttFixedHeader, variableHeader, payload);
    }

    @Override // io.netty.handler.codec.mqtt.MqttMessage
    public MqttMessageIdVariableHeader variableHeader() {
        return (MqttMessageIdVariableHeader) super.variableHeader();
    }

    @Override // io.netty.handler.codec.mqtt.MqttMessage
    public MqttSubAckPayload payload() {
        return (MqttSubAckPayload) super.payload();
    }
}
