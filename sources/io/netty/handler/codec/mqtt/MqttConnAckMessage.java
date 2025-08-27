package io.netty.handler.codec.mqtt;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/mqtt/MqttConnAckMessage.class */
public final class MqttConnAckMessage extends MqttMessage {
    public MqttConnAckMessage(MqttFixedHeader mqttFixedHeader, MqttConnAckVariableHeader variableHeader) {
        super(mqttFixedHeader, variableHeader);
    }

    @Override // io.netty.handler.codec.mqtt.MqttMessage
    public MqttConnAckVariableHeader variableHeader() {
        return (MqttConnAckVariableHeader) super.variableHeader();
    }
}
