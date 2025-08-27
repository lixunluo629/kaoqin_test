package com.moredian.onpremise.event;

import java.nio.ByteBuffer;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/event/DeviceHeartbeatEvent.class */
public class DeviceHeartbeatEvent extends IOTEvent {
    private static final long serialVersionUID = -9178173164672578409L;
    private long upTime;
    private String orgCode;

    @Override // com.moredian.onpremise.event.IOTEvent
    public String toString() {
        return "DeviceHeartbeatEvent(super=" + super.toString() + ", upTime=" + getUpTime() + ", orgCode=" + getOrgCode() + ")";
    }

    public DeviceHeartbeatEvent() {
        super(IOTEventType.HEARTBEAT.getType());
        this.upTime = System.currentTimeMillis();
    }

    public DeviceHeartbeatEvent(EventHeader header, ByteBuffer buffer) {
        super(header, buffer);
        this.upTime = System.currentTimeMillis();
    }

    @Override // com.moredian.onpremise.event.IOTEvent
    protected void parseBody() {
        super.parseBody();
        this.upTime = this.eventBody.getLong();
        this.orgCode = EventDecoder.bytes2String(this.eventBody, EventDecoder.byte2int(this.eventBody.get()));
    }

    @Override // com.moredian.onpremise.event.IOTEvent
    protected byte[] buildBody() {
        byte[] parent = super.buildBody();
        int len = parent.length;
        int len2 = len + 8;
        byte[] orgCode = this.orgCode.getBytes(DEFAULT_CHARSET);
        ByteBuffer buffer = ByteBuffer.allocate(len2 + 1 + orgCode.length);
        buffer.put(parent);
        buffer.putLong(this.upTime);
        buffer.put((byte) orgCode.length);
        buffer.put(orgCode);
        return buffer.array();
    }

    public long getUpTime() {
        return this.upTime;
    }

    public void setUpTime(long upTime) {
        this.upTime = upTime;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
}
