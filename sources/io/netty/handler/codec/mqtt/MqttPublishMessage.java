package io.netty.handler.codec.mqtt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.IllegalReferenceCountException;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/mqtt/MqttPublishMessage.class */
public class MqttPublishMessage extends MqttMessage implements ByteBufHolder {
    public MqttPublishMessage(MqttFixedHeader mqttFixedHeader, MqttPublishVariableHeader variableHeader, ByteBuf payload) {
        super(mqttFixedHeader, variableHeader, payload);
    }

    @Override // io.netty.handler.codec.mqtt.MqttMessage
    public MqttPublishVariableHeader variableHeader() {
        return (MqttPublishVariableHeader) super.variableHeader();
    }

    @Override // io.netty.handler.codec.mqtt.MqttMessage
    public ByteBuf payload() {
        return content();
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        ByteBuf data = (ByteBuf) super.payload();
        if (data.refCnt() <= 0) {
            throw new IllegalReferenceCountException(data.refCnt());
        }
        return data;
    }

    @Override // io.netty.buffer.ByteBufHolder
    public MqttPublishMessage copy() {
        return replace(content().copy());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public MqttPublishMessage duplicate() {
        return replace(content().duplicate());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public MqttPublishMessage retainedDuplicate() {
        return replace(content().retainedDuplicate());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public MqttPublishMessage replace(ByteBuf content) {
        return new MqttPublishMessage(fixedHeader(), variableHeader(), content);
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return content().refCnt();
    }

    @Override // io.netty.util.ReferenceCounted
    public MqttPublishMessage retain() {
        content().retain();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public MqttPublishMessage retain(int increment) {
        content().retain(increment);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public MqttPublishMessage touch() {
        content().touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public MqttPublishMessage touch(Object hint) {
        content().touch(hint);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return content().release();
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return content().release(decrement);
    }
}
