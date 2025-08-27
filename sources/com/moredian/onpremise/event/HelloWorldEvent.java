package com.moredian.onpremise.event;

import java.nio.ByteBuffer;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/event/HelloWorldEvent.class */
public class HelloWorldEvent extends IOTEvent {
    @Override // com.moredian.onpremise.event.IOTEvent
    public String toString() {
        return "HelloWorldEvent()";
    }

    public HelloWorldEvent() {
        super(IOTEventType.HELLO_WORLD.getType());
    }

    public HelloWorldEvent(EventHeader header, ByteBuffer buffer) {
        super(header, buffer);
    }
}
