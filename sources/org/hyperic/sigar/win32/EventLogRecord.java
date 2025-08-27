package org.hyperic.sigar.win32;

import java.util.Date;
import org.aspectj.weaver.Dump;
import org.springframework.beans.PropertyAccessor;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/EventLogRecord.class */
public class EventLogRecord {
    private static final String NA = "N/A";
    long recordNumber;
    long timeGenerated;
    long timeWritten;
    long eventId;
    short eventType;
    short category;
    String categoryString;
    String source;
    String computerName;
    String user;
    String message;
    String logName;

    EventLogRecord() {
    }

    public String getLogName() {
        return this.logName;
    }

    void setLogName(String logName) {
        this.logName = logName;
    }

    public long getRecordNumber() {
        return this.recordNumber;
    }

    public long getTimeGenerated() {
        return this.timeGenerated;
    }

    public long getTimeWritten() {
        return this.timeWritten;
    }

    public long getEventId() {
        return this.eventId;
    }

    public short getEventType() {
        return this.eventType;
    }

    public String getEventTypeString() {
        switch (this.eventType) {
            case 1:
                return "Error";
            case 2:
                return "Warning";
            case 4:
                return "Information";
            case 8:
                return "Success Audit";
            case 16:
                return "Failure Audit";
            default:
                return Dump.UNKNOWN_FILENAME;
        }
    }

    public short getCategory() {
        return this.category;
    }

    public String getCategoryString() {
        if (this.categoryString != null) {
            return this.categoryString.trim();
        }
        if (this.category == 0) {
            return "None";
        }
        return new StringBuffer().append("(").append((int) this.category).append(")").toString();
    }

    public String getSource() {
        return this.source;
    }

    public String getComputerName() {
        return this.computerName;
    }

    public String getUser() {
        return this.user;
    }

    private String getUserString() {
        if (this.user == null) {
            return NA;
        }
        return this.user;
    }

    public String getMessage() {
        return this.message;
    }

    public String getStringData() {
        return getMessage();
    }

    public String toString() {
        return new StringBuffer().append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(getEventTypeString()).append("] ").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(new Date(getTimeGenerated() * 1000)).append("] ").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(getSource()).append("] ").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(getCategoryString()).append("] ").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(getEventId() & 65535).append("] ").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(getUserString()).append("] ").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(getComputerName()).append("] ").append(getMessage()).toString();
    }
}
