package io.netty.handler.codec.mqtt;

import io.netty.handler.codec.DecoderException;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/mqtt/MqttUnacceptableProtocolVersionException.class */
public final class MqttUnacceptableProtocolVersionException extends DecoderException {
    private static final long serialVersionUID = 4914652213232455749L;

    public MqttUnacceptableProtocolVersionException() {
    }

    public MqttUnacceptableProtocolVersionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MqttUnacceptableProtocolVersionException(String message) {
        super(message);
    }

    public MqttUnacceptableProtocolVersionException(Throwable cause) {
        super(cause);
    }
}
