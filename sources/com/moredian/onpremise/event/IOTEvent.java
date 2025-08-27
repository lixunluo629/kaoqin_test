package com.moredian.onpremise.event;

import com.moredian.onpremise.core.common.constants.Constants;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/event/IOTEvent.class */
public class IOTEvent implements Serializable, EventTarget {
    private static final long serialVersionUID = 4105664619715211093L;
    public static String DEFAULT_STRING_ENCODING = "UTF-8";
    public static String DEFAULT_PACKAGE_ENCODING = "UTF-8";
    public static Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_STRING_ENCODING);
    public static byte V_0 = 0;
    public static byte V_1 = 1;
    protected String macAddress;
    protected String serialNumber;
    protected String softwareVersion;
    protected String romVersion;
    private String ipAddress;
    protected int eventType;
    protected int sequenceNumber;
    protected long timeStamp;
    protected byte version;
    protected byte[] reserved;
    protected EventHeader header;
    protected ByteBuffer eventBody;

    public String toString() {
        return "IOTEvent(macAddress=" + getMacAddress() + ", serialNumber=" + getSerialNumber() + ", softwareVersion=" + getSoftwareVersion() + ", romVersion=" + getRomVersion() + ", ipAddress=" + getIpAddress() + ", eventType=" + getEventType() + ", sequenceNumber=" + getSequenceNumber() + ", timeStamp=" + getTimeStamp() + ", version=" + ((int) getVersion()) + ", reserved=" + Arrays.toString(getReserved()) + ", header=" + getHeader() + ")";
    }

    public IOTEvent() {
        this.macAddress = "112233445566";
        this.serialNumber = Constants.DEFAULT_TARGET;
        this.softwareVersion = "0";
        this.romVersion = "0";
        this.ipAddress = "192.168.255.255";
        this.timeStamp = System.currentTimeMillis();
        this.version = V_1;
        this.reserved = new byte[4];
    }

    public IOTEvent(int eventType) {
        this.macAddress = "112233445566";
        this.serialNumber = Constants.DEFAULT_TARGET;
        this.softwareVersion = "0";
        this.romVersion = "0";
        this.ipAddress = "192.168.255.255";
        this.timeStamp = System.currentTimeMillis();
        this.version = V_1;
        this.reserved = new byte[4];
        setEventType(eventType);
    }

    public IOTEvent(byte[] payload) {
        this.macAddress = "112233445566";
        this.serialNumber = Constants.DEFAULT_TARGET;
        this.softwareVersion = "0";
        this.romVersion = "0";
        this.ipAddress = "192.168.255.255";
        this.timeStamp = System.currentTimeMillis();
        this.version = V_1;
        this.reserved = new byte[4];
        this.eventBody = ByteBuffer.wrap(payload);
        this.header = new EventHeader(this.eventBody);
        this.eventType = this.header.getEventType();
        this.sequenceNumber = this.header.getSequenceNumber();
        this.timeStamp = this.header.getTimeStamp();
        this.version = this.header.getVersion();
        this.reserved = this.header.getReserved();
    }

    public IOTEvent(EventHeader header, ByteBuffer buffer) {
        this.macAddress = "112233445566";
        this.serialNumber = Constants.DEFAULT_TARGET;
        this.softwareVersion = "0";
        this.romVersion = "0";
        this.ipAddress = "192.168.255.255";
        this.timeStamp = System.currentTimeMillis();
        this.version = V_1;
        this.reserved = new byte[4];
        this.eventType = header.getEventType();
        this.sequenceNumber = header.getSequenceNumber();
        this.timeStamp = header.getTimeStamp();
        this.version = header.getVersion();
        this.reserved = header.getReserved();
        this.header = header;
        this.eventBody = buffer;
    }

    public byte[] buildPacket() throws Exception {
        byte[] header = buildHeader();
        byte[] body = buildBody();
        byte[] packets = new byte[header.length + body.length];
        System.arraycopy(header, 0, packets, 0, header.length);
        System.arraycopy(body, 0, packets, header.length, body.length);
        return packets;
    }

    public void parsePacket() throws Exception {
        parseBody();
    }

    protected void parseBody() {
        this.macAddress = EventDecoder.bytes2String(this.eventBody, EventDecoder.byte2int(this.eventBody.get()));
        this.serialNumber = EventDecoder.bytes2String(this.eventBody, EventDecoder.byte2int(this.eventBody.get()));
        this.softwareVersion = EventDecoder.bytes2String(this.eventBody, EventDecoder.byte2int(this.eventBody.get()));
        this.romVersion = EventDecoder.bytes2String(this.eventBody, EventDecoder.byte2int(this.eventBody.get()));
        this.ipAddress = EventDecoder.bytes2String(this.eventBody, EventDecoder.byte2int(this.eventBody.get()));
    }

    protected byte[] buildHeader() {
        this.header = new EventHeader();
        this.header.setEventType(getEventType());
        this.header.setSequenceNumber(getSequenceNumber());
        this.header.setTimeStamp(getTimeStamp());
        this.header.setVersion(getVersion());
        this.header.setReserved(getReserved());
        return this.header.buildHeader();
    }

    protected byte[] buildBody() {
        byte[] mac;
        if (null == getMacAddress()) {
            mac = "112233445566".getBytes(DEFAULT_CHARSET);
        } else {
            mac = this.macAddress.getBytes(DEFAULT_CHARSET);
        }
        int len = 0 + 1 + mac.length;
        byte[] serial = this.serialNumber.getBytes(DEFAULT_CHARSET);
        int len2 = len + 1 + serial.length;
        byte[] sv = this.softwareVersion.getBytes(DEFAULT_CHARSET);
        int len3 = len2 + 1 + sv.length;
        byte[] rv = this.romVersion.getBytes(DEFAULT_CHARSET);
        int len4 = len3 + 1 + rv.length;
        byte[] ip = this.ipAddress.getBytes(DEFAULT_CHARSET);
        ByteBuffer buffer = ByteBuffer.allocate(len4 + 1 + ip.length);
        buffer.put((byte) mac.length);
        buffer.put(mac);
        buffer.put((byte) serial.length);
        buffer.put(serial);
        buffer.put((byte) sv.length);
        buffer.put(sv);
        buffer.put((byte) rv.length);
        buffer.put(rv);
        buffer.put((byte) ip.length);
        buffer.put(ip);
        return buffer.array();
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSoftwareVersion() {
        return this.softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
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

    public EventHeader getHeader() {
        return this.header;
    }

    public void setHeader(EventHeader header) {
        this.header = header;
    }

    public ByteBuffer getEventBody() {
        return this.eventBody;
    }

    public void setEventBody(ByteBuffer eventBody) {
        this.eventBody = eventBody;
    }

    public String getRomVersion() {
        return this.romVersion;
    }

    public void setRomVersion(String romVersion) {
        this.romVersion = romVersion;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override // com.moredian.onpremise.event.EventTarget
    public String target() {
        return getSerialNumber();
    }
}
