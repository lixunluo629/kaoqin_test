package org.hyperic.sigar.win32;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/EventLog.class */
public class EventLog extends Win32 {
    int eventLogHandle = 0;
    public static final String SYSTEM = "System";
    public static final String APPLICATION = "Application";
    public static final String SECURITY = "Security";
    public static final int EVENTLOG_SUCCESS = 0;
    public static final int EVENTLOG_ERROR_TYPE = 1;
    public static final int EVENTLOG_WARNING_TYPE = 2;
    public static final int EVENTLOG_INFORMATION_TYPE = 4;
    public static final int EVENTLOG_AUDIT_SUCCESS = 8;
    public static final int EVENTLOG_AUDIT_FAILURE = 16;
    public static final int EVENTLOG_WAIT_INFINITE = -1;
    private String name;

    public native void openlog(String str) throws Win32Exception;

    public native void close() throws Win32Exception;

    public native int getNumberOfRecords() throws Win32Exception;

    public native int getOldestRecord() throws Win32Exception;

    private native EventLogRecord readlog(String str, int i) throws Win32Exception;

    public native void waitForChange(int i) throws Win32Exception;

    public void open(String name) throws Win32Exception {
        this.name = name;
        openlog(name);
    }

    public int getNewestRecord() throws Win32Exception {
        return (getOldestRecord() + getNumberOfRecords()) - 1;
    }

    public EventLogRecord read(int recordOffset) throws Win32Exception {
        EventLogRecord record = readlog(this.name, recordOffset);
        record.setLogName(this.name);
        return record;
    }

    public static String[] getLogNames() {
        String[] names;
        RegistryKey key = null;
        try {
            try {
                key = RegistryKey.LocalMachine.openSubKey("SYSTEM\\CurrentControlSet\\Services\\Eventlog");
                names = key.getSubKeyNames();
                if (key != null) {
                    key.close();
                }
            } catch (Win32Exception e) {
                names = new String[]{SYSTEM, APPLICATION, SECURITY};
                if (key != null) {
                    key.close();
                }
            }
            return names;
        } catch (Throwable th) {
            if (key != null) {
                key.close();
            }
            throw th;
        }
    }
}
