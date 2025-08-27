package com.moredian.onpremise.event;

import java.nio.ByteBuffer;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/event/AckFromDeviceEvent.class */
public class AckFromDeviceEvent extends IOTEvent {
    private static final long serialVersionUID = 4813898040913368003L;
    private int originEventType;

    public AckFromDeviceEvent() {
        super(IOTEventType.ACK_FROM_DEVICE.getType());
    }

    public AckFromDeviceEvent(EventHeader header, ByteBuffer buffer) {
        super(header, buffer);
    }

    public AckFromDeviceEvent(byte[] packet) {
        super(packet);
    }

    @Override // com.moredian.onpremise.event.IOTEvent
    protected byte[] buildBody() {
        byte[] parent = super.buildBody();
        int len = parent.length;
        ByteBuffer buffer = ByteBuffer.allocate(len + 4);
        buffer.put(parent);
        buffer.putInt(this.originEventType);
        return buffer.array();
    }

    @Override // com.moredian.onpremise.event.IOTEvent
    protected void parseBody() {
        super.parseBody();
        this.originEventType = this.eventBody.getInt();
    }

    public int getOriginEventType() {
        return this.originEventType;
    }

    public void setOriginEventType(int originEventType) {
        this.originEventType = originEventType;
    }
}
