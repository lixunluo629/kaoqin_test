package com.moredian.onpremise.event;

import java.nio.ByteBuffer;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/event/EventFactory.class */
public class EventFactory {
    public static IOTEvent getEvent(byte[] packet) throws Exception {
        return getEvent(ByteBuffer.wrap(packet));
    }

    public static IOTEvent getEvent(ByteBuffer buffer) throws Exception {
        EventHeader header = new EventHeader(buffer);
        IOTEvent event = null;
        switch (IOTEventType.fromType(header.getEventType())) {
            case HEARTBEAT:
                event = new DeviceHeartbeatEvent(header, buffer);
                break;
            case HELLO_WORLD:
                event = new HelloWorldEvent(header, buffer);
                break;
            case MODEL_TRANSFER:
                event = new ModelTransferEvent(header, buffer);
                break;
        }
        if (event != null) {
            event.parsePacket();
        }
        return event;
    }
}
