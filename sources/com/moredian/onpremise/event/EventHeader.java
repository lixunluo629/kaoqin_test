package com.moredian.onpremise.event;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.sql.Timestamp;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/event/EventHeader.class */
public class EventHeader implements Serializable {
    private static final long serialVersionUID = 6161872339779334725L;
    public static final int HEADER_LENGTH = 17;
    int eventType;
    int sequenceNumber;
    long timeStamp;
    byte version;
    byte[] reserved;

    public EventHeader() {
        this.version = (byte) 0;
        this.reserved = new byte[4];
    }

    public EventHeader(ByteBuffer buf) {
        this.version = (byte) 0;
        this.reserved = new byte[4];
        this.eventType = buf.getInt();
        this.sequenceNumber = buf.getInt();
        this.timeStamp = EventDecoder.int2long(buf.getInt()) * 1000;
        if (this.timeStamp < System.currentTimeMillis() - 31536000000L || this.timeStamp > System.currentTimeMillis() + 86400000) {
            this.timeStamp = System.currentTimeMillis();
        }
        this.version = buf.get();
        this.reserved = new byte[4];
        buf.get(this.reserved);
    }

    public byte[] buildHeader() {
        ByteBuffer buffer = ByteBuffer.allocate(17);
        buffer.putInt(getEventType());
        buffer.putInt(getSequenceNumber());
        int ts = (int) (getTimeStamp() / 1000);
        buffer.putInt(ts);
        buffer.put(getVersion());
        buffer.put(getReserved());
        return buffer.array();
    }

    public String toString() {
        Timestamp ts = new Timestamp(this.timeStamp);
        return String.format("%d | %d | %s", Integer.valueOf(this.eventType), Integer.valueOf(this.sequenceNumber), ts.toString());
    }

    public int getEventType() {
        return this.eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public byte getVersion() {
        return this.version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte[] getReserved() {
        return this.reserved;
    }

    public void setReserved(byte[] reserved) {
        this.reserved = reserved;
    }
}
