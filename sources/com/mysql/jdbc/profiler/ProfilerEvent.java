package com.mysql.jdbc.profiler;

import com.mysql.jdbc.StringUtils;
import com.mysql.jdbc.log.LogUtils;
import java.util.Date;
import org.springframework.beans.PropertyAccessor;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/profiler/ProfilerEvent.class */
public class ProfilerEvent {
    public static final byte TYPE_USAGE = 0;
    public static final byte TYPE_WARN = 0;
    public static final byte TYPE_OBJECT_CREATION = 1;
    public static final byte TYPE_PREPARE = 2;
    public static final byte TYPE_QUERY = 3;
    public static final byte TYPE_EXECUTE = 4;
    public static final byte TYPE_FETCH = 5;
    public static final byte TYPE_SLOW_QUERY = 6;
    public static final byte NA = -1;
    protected byte eventType;
    protected String hostName;
    protected String catalog;
    protected long connectionId;
    protected int statementId;
    protected int resultSetId;
    protected long eventCreationTime;
    protected long eventDuration;
    protected String durationUnits;
    protected String eventCreationPointDesc;
    protected String message;
    public int hostNameIndex;
    public int catalogIndex;
    public int eventCreationPointIndex;

    public ProfilerEvent(byte eventType, String hostName, String catalog, long connectionId, int statementId, int resultSetId, long eventDuration, String durationUnits, Throwable eventCreationPoint, String message) {
        this(eventType, hostName, catalog, connectionId, statementId, resultSetId, System.currentTimeMillis(), eventDuration, durationUnits, LogUtils.findCallingClassAndMethod(eventCreationPoint), message, -1, -1, -1);
    }

    private ProfilerEvent(byte eventType, String hostName, String catalog, long connectionId, int statementId, int resultSetId, long eventCreationTime, long eventDuration, String durationUnits, String eventCreationPointDesc, String message, int hostNameIndex, int catalogIndex, int eventCreationPointIndex) {
        this.eventType = eventType;
        this.hostName = hostName == null ? "" : hostName;
        this.catalog = catalog == null ? "" : catalog;
        this.connectionId = connectionId;
        this.statementId = statementId;
        this.resultSetId = resultSetId;
        this.eventCreationTime = eventCreationTime;
        this.eventDuration = eventDuration;
        this.durationUnits = durationUnits == null ? "" : durationUnits;
        this.eventCreationPointDesc = eventCreationPointDesc == null ? "" : eventCreationPointDesc;
        this.message = message == null ? "" : message;
        this.hostNameIndex = hostNameIndex;
        this.catalogIndex = catalogIndex;
        this.eventCreationPointIndex = eventCreationPointIndex;
    }

    public byte getEventType() {
        return this.eventType;
    }

    public String getHostName() {
        return this.hostName;
    }

    public String getCatalog() {
        return this.catalog;
    }

    public long getConnectionId() {
        return this.connectionId;
    }

    public int getStatementId() {
        return this.statementId;
    }

    public int getResultSetId() {
        return this.resultSetId;
    }

    public long getEventCreationTime() {
        return this.eventCreationTime;
    }

    public long getEventDuration() {
        return this.eventDuration;
    }

    public String getDurationUnits() {
        return this.durationUnits;
    }

    public String getEventCreationPointAsString() {
        return this.eventCreationPointDesc;
    }

    public String getMessage() {
        return this.message;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(PropertyAccessor.PROPERTY_KEY_PREFIX);
        switch (getEventType()) {
            case 0:
                buf.append("USAGE ADVISOR");
                break;
            case 1:
                buf.append("CONSTRUCT");
                break;
            case 2:
                buf.append("PREPARE");
                break;
            case 3:
                buf.append("QUERY");
                break;
            case 4:
                buf.append("EXECUTE");
                break;
            case 5:
                buf.append("FETCH");
                break;
            case 6:
                buf.append("SLOW QUERY");
                break;
            default:
                buf.append("UNKNOWN");
                break;
        }
        buf.append("] ");
        buf.append(this.message);
        buf.append(" [Created on: ");
        buf.append(new Date(this.eventCreationTime));
        buf.append(", duration: ");
        buf.append(this.eventDuration);
        buf.append(", connection-id: ");
        buf.append(this.connectionId);
        buf.append(", statement-id: ");
        buf.append(this.statementId);
        buf.append(", resultset-id: ");
        buf.append(this.resultSetId);
        buf.append(",");
        buf.append(this.eventCreationPointDesc);
        buf.append(", hostNameIndex: ");
        buf.append(this.hostNameIndex);
        buf.append(", catalogIndex: ");
        buf.append(this.catalogIndex);
        buf.append(", eventCreationPointIndex: ");
        buf.append(this.eventCreationPointIndex);
        buf.append("]");
        return buf.toString();
    }

    public static ProfilerEvent unpack(byte[] buf) throws Exception {
        int pos = 0 + 1;
        byte eventType = buf[0];
        byte[] host = readBytes(buf, pos);
        int pos2 = pos + 4 + host.length;
        byte[] db = readBytes(buf, pos2);
        int pos3 = pos2 + 4 + db.length;
        long connectionId = readLong(buf, pos3);
        int pos4 = pos3 + 8;
        int statementId = readInt(buf, pos4);
        int pos5 = pos4 + 4;
        int resultSetId = readInt(buf, pos5);
        int pos6 = pos5 + 4;
        long eventCreationTime = readLong(buf, pos6);
        int pos7 = pos6 + 8;
        long eventDuration = readLong(buf, pos7);
        int pos8 = pos7 + 8;
        byte[] eventDurationUnits = readBytes(buf, pos8);
        int pos9 = pos8 + 4 + eventDurationUnits.length;
        byte[] eventCreationAsBytes = readBytes(buf, pos9);
        int pos10 = pos9 + 4 + eventCreationAsBytes.length;
        byte[] message = readBytes(buf, pos10);
        int pos11 = pos10 + 4 + message.length;
        int hostNameIndex = readInt(buf, pos11);
        int pos12 = pos11 + 4;
        int catalogIndex = readInt(buf, pos12);
        int pos13 = pos12 + 4;
        int eventCreationPointIndex = readInt(buf, pos13);
        int i = pos13 + 4;
        return new ProfilerEvent(eventType, StringUtils.toString(host, "ISO8859_1"), StringUtils.toString(db, "ISO8859_1"), connectionId, statementId, resultSetId, eventCreationTime, eventDuration, StringUtils.toString(eventDurationUnits, "ISO8859_1"), StringUtils.toString(eventCreationAsBytes, "ISO8859_1"), StringUtils.toString(message, "ISO8859_1"), hostNameIndex, catalogIndex, eventCreationPointIndex);
    }

    public byte[] pack() throws Exception {
        byte[] hostNameAsBytes = StringUtils.getBytes(this.hostName, "ISO8859_1");
        byte[] dbAsBytes = StringUtils.getBytes(this.catalog, "ISO8859_1");
        byte[] durationUnitsAsBytes = StringUtils.getBytes(this.durationUnits, "ISO8859_1");
        byte[] eventCreationAsBytes = StringUtils.getBytes(this.eventCreationPointDesc, "ISO8859_1");
        byte[] messageAsBytes = StringUtils.getBytes(this.message, "ISO8859_1");
        int len = 1 + 4 + hostNameAsBytes.length + 4 + dbAsBytes.length + 8 + 4 + 4 + 8 + 8 + 4 + durationUnitsAsBytes.length + 4 + eventCreationAsBytes.length + 4 + messageAsBytes.length + 4 + 4 + 4;
        byte[] buf = new byte[len];
        int pos = 0 + 1;
        buf[0] = this.eventType;
        writeInt(this.eventCreationPointIndex, buf, writeInt(this.catalogIndex, buf, writeInt(this.hostNameIndex, buf, writeBytes(messageAsBytes, buf, writeBytes(eventCreationAsBytes, buf, writeBytes(durationUnitsAsBytes, buf, writeLong(this.eventDuration, buf, writeLong(this.eventCreationTime, buf, writeInt(this.resultSetId, buf, writeInt(this.statementId, buf, writeLong(this.connectionId, buf, writeBytes(dbAsBytes, buf, writeBytes(hostNameAsBytes, buf, pos)))))))))))));
        return buf;
    }

    private static int writeInt(int i, byte[] buf, int pos) {
        int pos2 = pos + 1;
        buf[pos] = (byte) (i & 255);
        int pos3 = pos2 + 1;
        buf[pos2] = (byte) (i >>> 8);
        int pos4 = pos3 + 1;
        buf[pos3] = (byte) (i >>> 16);
        int pos5 = pos4 + 1;
        buf[pos4] = (byte) (i >>> 24);
        return pos5;
    }

    private static int writeLong(long l, byte[] buf, int pos) {
        int pos2 = pos + 1;
        buf[pos] = (byte) (l & 255);
        int pos3 = pos2 + 1;
        buf[pos2] = (byte) (l >>> 8);
        int pos4 = pos3 + 1;
        buf[pos3] = (byte) (l >>> 16);
        int pos5 = pos4 + 1;
        buf[pos4] = (byte) (l >>> 24);
        int pos6 = pos5 + 1;
        buf[pos5] = (byte) (l >>> 32);
        int pos7 = pos6 + 1;
        buf[pos6] = (byte) (l >>> 40);
        int pos8 = pos7 + 1;
        buf[pos7] = (byte) (l >>> 48);
        int pos9 = pos8 + 1;
        buf[pos8] = (byte) (l >>> 56);
        return pos9;
    }

    private static int writeBytes(byte[] msg, byte[] buf, int pos) {
        int pos2 = writeInt(msg.length, buf, pos);
        System.arraycopy(msg, 0, buf, pos2, msg.length);
        return pos2 + msg.length;
    }

    private static int readInt(byte[] buf, int pos) {
        int pos2 = pos + 1;
        int i = buf[pos] & 255;
        int pos3 = pos2 + 1;
        int i2 = i | ((buf[pos2] & 255) << 8);
        int pos4 = pos3 + 1;
        int i3 = i2 | ((buf[pos3] & 255) << 16);
        int i4 = pos4 + 1;
        return i3 | ((buf[pos4] & 255) << 24);
    }

    private static long readLong(byte[] buf, int pos) {
        long j = buf[pos] & 255;
        long j2 = j | ((buf[r7] & 255) << 8);
        long j3 = j2 | ((buf[r7] & 255) << 16);
        long j4 = j3 | ((buf[r7] & 255) << 24);
        long j5 = j4 | ((buf[r7] & 255) << 32);
        long j6 = j5 | ((buf[r7] & 255) << 40);
        long j7 = j6 | ((buf[r7] & 255) << 48);
        int i = pos + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1;
        return j7 | ((buf[r7] & 255) << 56);
    }

    private static byte[] readBytes(byte[] buf, int pos) {
        int length = readInt(buf, pos);
        byte[] msg = new byte[length];
        System.arraycopy(buf, pos + 4, msg, 0, length);
        return msg;
    }
}
