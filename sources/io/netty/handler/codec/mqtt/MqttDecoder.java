package io.netty.handler.codec.mqtt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Font;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/mqtt/MqttDecoder.class */
public final class MqttDecoder extends ReplayingDecoder<DecoderState> {
    private static final int DEFAULT_MAX_BYTES_IN_MESSAGE = 8092;
    private MqttFixedHeader mqttFixedHeader;
    private Object variableHeader;
    private int bytesRemainingInVariablePart;
    private final int maxBytesInMessage;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/mqtt/MqttDecoder$DecoderState.class */
    enum DecoderState {
        READ_FIXED_HEADER,
        READ_VARIABLE_HEADER,
        READ_PAYLOAD,
        BAD_MESSAGE
    }

    public MqttDecoder() {
        this(DEFAULT_MAX_BYTES_IN_MESSAGE);
    }

    public MqttDecoder(int maxBytesInMessage) {
        super(DecoderState.READ_FIXED_HEADER);
        this.maxBytesInMessage = maxBytesInMessage;
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        switch (state()) {
            case READ_FIXED_HEADER:
                try {
                    this.mqttFixedHeader = decodeFixedHeader(buffer);
                    this.bytesRemainingInVariablePart = this.mqttFixedHeader.remainingLength();
                    checkpoint(DecoderState.READ_VARIABLE_HEADER);
                } catch (Exception cause) {
                    out.add(invalidMessage(cause));
                    return;
                }
            case READ_VARIABLE_HEADER:
                try {
                    Result<?> decodedVariableHeader = decodeVariableHeader(buffer, this.mqttFixedHeader);
                    this.variableHeader = ((Result) decodedVariableHeader).value;
                    if (this.bytesRemainingInVariablePart > this.maxBytesInMessage) {
                        throw new DecoderException("too large message: " + this.bytesRemainingInVariablePart + " bytes");
                    }
                    this.bytesRemainingInVariablePart -= ((Result) decodedVariableHeader).numberOfBytesConsumed;
                    checkpoint(DecoderState.READ_PAYLOAD);
                } catch (Exception cause2) {
                    out.add(invalidMessage(cause2));
                    return;
                }
            case READ_PAYLOAD:
                try {
                    Result<?> decodedPayload = decodePayload(buffer, this.mqttFixedHeader.messageType(), this.bytesRemainingInVariablePart, this.variableHeader);
                    this.bytesRemainingInVariablePart -= ((Result) decodedPayload).numberOfBytesConsumed;
                    if (this.bytesRemainingInVariablePart != 0) {
                        throw new DecoderException("non-zero remaining payload bytes: " + this.bytesRemainingInVariablePart + " (" + this.mqttFixedHeader.messageType() + ')');
                    }
                    checkpoint(DecoderState.READ_FIXED_HEADER);
                    MqttMessage message = MqttMessageFactory.newMessage(this.mqttFixedHeader, this.variableHeader, ((Result) decodedPayload).value);
                    this.mqttFixedHeader = null;
                    this.variableHeader = null;
                    out.add(message);
                    return;
                } catch (Exception cause3) {
                    out.add(invalidMessage(cause3));
                    return;
                }
            case BAD_MESSAGE:
                buffer.skipBytes(actualReadableBytes());
                return;
            default:
                throw new Error();
        }
    }

    private MqttMessage invalidMessage(Throwable cause) {
        checkpoint(DecoderState.BAD_MESSAGE);
        return MqttMessageFactory.newInvalidMessage(this.mqttFixedHeader, this.variableHeader, cause);
    }

    private static MqttFixedHeader decodeFixedHeader(ByteBuf buffer) {
        short digit;
        short b1 = buffer.readUnsignedByte();
        MqttMessageType messageType = MqttMessageType.valueOf(b1 >> 4);
        boolean dupFlag = (b1 & 8) == 8;
        int qosLevel = (b1 & 6) >> 1;
        boolean retain = (b1 & 1) != 0;
        int remainingLength = 0;
        int multiplier = 1;
        int loops = 0;
        do {
            digit = buffer.readUnsignedByte();
            remainingLength += (digit & 127) * multiplier;
            multiplier *= 128;
            loops++;
            if ((digit & 128) == 0) {
                break;
            }
        } while (loops < 4);
        if (loops == 4 && (digit & 128) != 0) {
            throw new DecoderException("remaining length exceeds 4 digits (" + messageType + ')');
        }
        MqttFixedHeader decodedFixedHeader = new MqttFixedHeader(messageType, dupFlag, MqttQoS.valueOf(qosLevel), retain, remainingLength);
        return MqttCodecUtil.validateFixedHeader(MqttCodecUtil.resetUnusedFields(decodedFixedHeader));
    }

    private static Result<?> decodeVariableHeader(ByteBuf buffer, MqttFixedHeader mqttFixedHeader) {
        switch (mqttFixedHeader.messageType()) {
            case CONNECT:
                return decodeConnectionVariableHeader(buffer);
            case CONNACK:
                return decodeConnAckVariableHeader(buffer);
            case SUBSCRIBE:
            case UNSUBSCRIBE:
            case SUBACK:
            case UNSUBACK:
            case PUBACK:
            case PUBREC:
            case PUBCOMP:
            case PUBREL:
                return decodeMessageIdVariableHeader(buffer);
            case PUBLISH:
                return decodePublishVariableHeader(buffer, mqttFixedHeader);
            case PINGREQ:
            case PINGRESP:
            case DISCONNECT:
                return new Result<>(null, 0);
            default:
                return new Result<>(null, 0);
        }
    }

    private static Result<MqttConnectVariableHeader> decodeConnectionVariableHeader(ByteBuf buffer) {
        Result<String> protoString = decodeString(buffer);
        int numberOfBytesConsumed = ((Result) protoString).numberOfBytesConsumed;
        byte protocolLevel = buffer.readByte();
        int numberOfBytesConsumed2 = numberOfBytesConsumed + 1;
        MqttVersion mqttVersion = MqttVersion.fromProtocolNameAndLevel((String) ((Result) protoString).value, protocolLevel);
        int b1 = buffer.readUnsignedByte();
        Result<Integer> keepAlive = decodeMsbLsb(buffer);
        int numberOfBytesConsumed3 = numberOfBytesConsumed2 + 1 + ((Result) keepAlive).numberOfBytesConsumed;
        boolean hasUserName = (b1 & 128) == 128;
        boolean hasPassword = (b1 & 64) == 64;
        boolean willRetain = (b1 & 32) == 32;
        int willQos = (b1 & 24) >> 3;
        boolean willFlag = (b1 & 4) == 4;
        boolean cleanSession = (b1 & 2) == 2;
        if (mqttVersion == MqttVersion.MQTT_3_1_1) {
            boolean zeroReservedFlag = (b1 & 1) == 0;
            if (!zeroReservedFlag) {
                throw new DecoderException("non-zero reserved flag");
            }
        }
        MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader(mqttVersion.protocolName(), mqttVersion.protocolLevel(), hasUserName, hasPassword, willRetain, willQos, willFlag, cleanSession, ((Integer) ((Result) keepAlive).value).intValue());
        return new Result<>(mqttConnectVariableHeader, numberOfBytesConsumed3);
    }

    private static Result<MqttConnAckVariableHeader> decodeConnAckVariableHeader(ByteBuf buffer) {
        boolean sessionPresent = (buffer.readUnsignedByte() & 1) == 1;
        byte returnCode = buffer.readByte();
        MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(MqttConnectReturnCode.valueOf(returnCode), sessionPresent);
        return new Result<>(mqttConnAckVariableHeader, 2);
    }

    private static Result<MqttMessageIdVariableHeader> decodeMessageIdVariableHeader(ByteBuf buffer) {
        Result<Integer> messageId = decodeMessageId(buffer);
        return new Result<>(MqttMessageIdVariableHeader.from(((Integer) ((Result) messageId).value).intValue()), ((Result) messageId).numberOfBytesConsumed);
    }

    private static Result<MqttPublishVariableHeader> decodePublishVariableHeader(ByteBuf buffer, MqttFixedHeader mqttFixedHeader) {
        Result<String> decodedTopic = decodeString(buffer);
        if (!MqttCodecUtil.isValidPublishTopicName((String) ((Result) decodedTopic).value)) {
            throw new DecoderException("invalid publish topic name: " + ((String) ((Result) decodedTopic).value) + " (contains wildcards)");
        }
        int numberOfBytesConsumed = ((Result) decodedTopic).numberOfBytesConsumed;
        int messageId = -1;
        if (mqttFixedHeader.qosLevel().value() > 0) {
            Result<Integer> decodedMessageId = decodeMessageId(buffer);
            messageId = ((Integer) ((Result) decodedMessageId).value).intValue();
            numberOfBytesConsumed += ((Result) decodedMessageId).numberOfBytesConsumed;
        }
        MqttPublishVariableHeader mqttPublishVariableHeader = new MqttPublishVariableHeader((String) ((Result) decodedTopic).value, messageId);
        return new Result<>(mqttPublishVariableHeader, numberOfBytesConsumed);
    }

    private static Result<Integer> decodeMessageId(ByteBuf buffer) {
        Result<Integer> messageId = decodeMsbLsb(buffer);
        if (!MqttCodecUtil.isValidMessageId(((Integer) ((Result) messageId).value).intValue())) {
            throw new DecoderException("invalid messageId: " + ((Result) messageId).value);
        }
        return messageId;
    }

    private static Result<?> decodePayload(ByteBuf buffer, MqttMessageType messageType, int bytesRemainingInVariablePart, Object variableHeader) {
        switch (messageType) {
            case CONNECT:
                return decodeConnectionPayload(buffer, (MqttConnectVariableHeader) variableHeader);
            case CONNACK:
            case UNSUBACK:
            case PUBACK:
            case PUBREC:
            case PUBCOMP:
            case PUBREL:
            default:
                return new Result<>(null, 0);
            case SUBSCRIBE:
                return decodeSubscribePayload(buffer, bytesRemainingInVariablePart);
            case UNSUBSCRIBE:
                return decodeUnsubscribePayload(buffer, bytesRemainingInVariablePart);
            case SUBACK:
                return decodeSubackPayload(buffer, bytesRemainingInVariablePart);
            case PUBLISH:
                return decodePublishPayload(buffer, bytesRemainingInVariablePart);
        }
    }

    private static Result<MqttConnectPayload> decodeConnectionPayload(ByteBuf buffer, MqttConnectVariableHeader mqttConnectVariableHeader) {
        Result<String> decodedClientId = decodeString(buffer);
        String decodedClientIdValue = (String) ((Result) decodedClientId).value;
        MqttVersion mqttVersion = MqttVersion.fromProtocolNameAndLevel(mqttConnectVariableHeader.name(), (byte) mqttConnectVariableHeader.version());
        if (!MqttCodecUtil.isValidClientId(mqttVersion, decodedClientIdValue)) {
            throw new MqttIdentifierRejectedException("invalid clientIdentifier: " + decodedClientIdValue);
        }
        int numberOfBytesConsumed = ((Result) decodedClientId).numberOfBytesConsumed;
        Result<String> decodedWillTopic = null;
        Result<byte[]> decodedWillMessage = null;
        if (mqttConnectVariableHeader.isWillFlag()) {
            decodedWillTopic = decodeString(buffer, 0, Font.COLOR_NORMAL);
            int numberOfBytesConsumed2 = numberOfBytesConsumed + ((Result) decodedWillTopic).numberOfBytesConsumed;
            decodedWillMessage = decodeByteArray(buffer);
            numberOfBytesConsumed = numberOfBytesConsumed2 + ((Result) decodedWillMessage).numberOfBytesConsumed;
        }
        Result<String> decodedUserName = null;
        Result<byte[]> decodedPassword = null;
        if (mqttConnectVariableHeader.hasUserName()) {
            decodedUserName = decodeString(buffer);
            numberOfBytesConsumed += ((Result) decodedUserName).numberOfBytesConsumed;
        }
        if (mqttConnectVariableHeader.hasPassword()) {
            decodedPassword = decodeByteArray(buffer);
            numberOfBytesConsumed += ((Result) decodedPassword).numberOfBytesConsumed;
        }
        MqttConnectPayload mqttConnectPayload = new MqttConnectPayload((String) ((Result) decodedClientId).value, decodedWillTopic != null ? (String) ((Result) decodedWillTopic).value : null, decodedWillMessage != null ? (byte[]) ((Result) decodedWillMessage).value : null, decodedUserName != null ? (String) ((Result) decodedUserName).value : null, decodedPassword != null ? (byte[]) ((Result) decodedPassword).value : null);
        return new Result<>(mqttConnectPayload, numberOfBytesConsumed);
    }

    private static Result<MqttSubscribePayload> decodeSubscribePayload(ByteBuf buffer, int bytesRemainingInVariablePart) {
        List<MqttTopicSubscription> subscribeTopics = new ArrayList<>();
        int numberOfBytesConsumed = 0;
        while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
            Result<String> decodedTopicName = decodeString(buffer);
            int numberOfBytesConsumed2 = numberOfBytesConsumed + ((Result) decodedTopicName).numberOfBytesConsumed;
            int qos = buffer.readUnsignedByte() & 3;
            numberOfBytesConsumed = numberOfBytesConsumed2 + 1;
            subscribeTopics.add(new MqttTopicSubscription((String) ((Result) decodedTopicName).value, MqttQoS.valueOf(qos)));
        }
        return new Result<>(new MqttSubscribePayload(subscribeTopics), numberOfBytesConsumed);
    }

    private static Result<MqttSubAckPayload> decodeSubackPayload(ByteBuf buffer, int bytesRemainingInVariablePart) {
        List<Integer> grantedQos = new ArrayList<>();
        int numberOfBytesConsumed = 0;
        while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
            int qos = buffer.readUnsignedByte();
            if (qos != MqttQoS.FAILURE.value()) {
                qos &= 3;
            }
            numberOfBytesConsumed++;
            grantedQos.add(Integer.valueOf(qos));
        }
        return new Result<>(new MqttSubAckPayload(grantedQos), numberOfBytesConsumed);
    }

    private static Result<MqttUnsubscribePayload> decodeUnsubscribePayload(ByteBuf buffer, int bytesRemainingInVariablePart) {
        ArrayList arrayList = new ArrayList();
        int numberOfBytesConsumed = 0;
        while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
            Result<String> decodedTopicName = decodeString(buffer);
            numberOfBytesConsumed += ((Result) decodedTopicName).numberOfBytesConsumed;
            arrayList.add(((Result) decodedTopicName).value);
        }
        return new Result<>(new MqttUnsubscribePayload(arrayList), numberOfBytesConsumed);
    }

    private static Result<ByteBuf> decodePublishPayload(ByteBuf buffer, int bytesRemainingInVariablePart) {
        ByteBuf b = buffer.readRetainedSlice(bytesRemainingInVariablePart);
        return new Result<>(b, bytesRemainingInVariablePart);
    }

    private static Result<String> decodeString(ByteBuf buffer) {
        return decodeString(buffer, 0, Integer.MAX_VALUE);
    }

    private static Result<String> decodeString(ByteBuf buffer, int minBytes, int maxBytes) {
        Result<Integer> decodedSize = decodeMsbLsb(buffer);
        int size = ((Integer) ((Result) decodedSize).value).intValue();
        int numberOfBytesConsumed = ((Result) decodedSize).numberOfBytesConsumed;
        if (size < minBytes || size > maxBytes) {
            buffer.skipBytes(size);
            return new Result<>(null, numberOfBytesConsumed + size);
        }
        String s = buffer.toString(buffer.readerIndex(), size, CharsetUtil.UTF_8);
        buffer.skipBytes(size);
        return new Result<>(s, numberOfBytesConsumed + size);
    }

    private static Result<byte[]> decodeByteArray(ByteBuf buffer) {
        Result<Integer> decodedSize = decodeMsbLsb(buffer);
        int size = ((Integer) ((Result) decodedSize).value).intValue();
        byte[] bytes = new byte[size];
        buffer.readBytes(bytes);
        return new Result<>(bytes, ((Result) decodedSize).numberOfBytesConsumed + size);
    }

    private static Result<Integer> decodeMsbLsb(ByteBuf buffer) {
        return decodeMsbLsb(buffer, 0, 65535);
    }

    private static Result<Integer> decodeMsbLsb(ByteBuf buffer, int min, int max) {
        short msbSize = buffer.readUnsignedByte();
        short lsbSize = buffer.readUnsignedByte();
        int result = (msbSize << 8) | lsbSize;
        if (result < min || result > max) {
            result = -1;
        }
        return new Result<>(Integer.valueOf(result), 2);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/mqtt/MqttDecoder$Result.class */
    private static final class Result<T> {
        private final T value;
        private final int numberOfBytesConsumed;

        Result(T value, int numberOfBytesConsumed) {
            this.value = value;
            this.numberOfBytesConsumed = numberOfBytesConsumed;
        }
    }
}
