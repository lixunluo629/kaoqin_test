package org.hyperic.sigar.win32;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/EventLogNotification.class */
public interface EventLogNotification {
    boolean matches(EventLogRecord eventLogRecord);

    void handleNotification(EventLogRecord eventLogRecord);
}
