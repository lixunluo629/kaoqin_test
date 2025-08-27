package org.hyperic.sigar.win32.test;

import org.hyperic.sigar.test.SigarTestCase;
import org.hyperic.sigar.win32.EventLog;
import org.hyperic.sigar.win32.EventLogNotification;
import org.hyperic.sigar.win32.EventLogRecord;
import org.hyperic.sigar.win32.EventLogThread;
import org.hyperic.sigar.win32.Win32Exception;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/test/TestEventLog.class */
public class TestEventLog extends SigarTestCase {

    /* renamed from: org.hyperic.sigar.win32.test.TestEventLog$1, reason: invalid class name */
    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/test/TestEventLog$1.class */
    static class AnonymousClass1 {
    }

    public TestEventLog(String name) {
        super(name);
    }

    public void testOpenClose() throws Exception {
        EventLog log = new EventLog();
        try {
            log.close();
            fail("Closing an unopened event log succeeded");
        } catch (Win32Exception e) {
        }
        log.open(EventLog.APPLICATION);
        log.close();
        log.open(EventLog.SYSTEM);
        log.close();
    }

    public void testGetNumberOfRecords() throws Exception {
        EventLog log = new EventLog();
        log.open(EventLog.APPLICATION);
        try {
            log.getNumberOfRecords();
        } catch (Exception e) {
            fail("Unable to get the number of records");
        }
        log.close();
    }

    public void testGetOldestRecord() throws Exception {
        EventLog log = new EventLog();
        log.open(EventLog.APPLICATION);
        try {
            log.getOldestRecord();
        } catch (Exception e) {
            fail("Unable to get the oldest event record");
        }
        log.close();
    }

    public void testGetNewestRecord() throws Exception {
        EventLog log = new EventLog();
        log.open(EventLog.APPLICATION);
        try {
            log.getNewestRecord();
        } catch (Exception e) {
            fail("Unable to get the newest event record");
        }
        log.close();
    }

    private int readAll(String logname) throws Exception {
        int fail = 0;
        int success = 0;
        String testMax = System.getProperty("sigar.testeventlog.max");
        int max = testMax != null ? Integer.parseInt(testMax) : 500;
        EventLog log = new EventLog();
        log.open(logname);
        if (log.getNumberOfRecords() == 0) {
            log.close();
            return 0;
        }
        int oldestRecord = log.getOldestRecord();
        int numRecords = log.getNumberOfRecords();
        traceln(new StringBuffer().append("oldest=").append(oldestRecord).append(", total=").append(numRecords).append(", max=").append(max).toString());
        for (int i = oldestRecord; i < oldestRecord + numRecords; i++) {
            try {
                log.read(i);
                success++;
            } catch (Win32Exception e) {
                fail++;
                traceln(new StringBuffer().append("Error reading record ").append(i).append(": ").append(e.getMessage()).toString());
            }
            if (success > max) {
                break;
            }
        }
        log.close();
        traceln(new StringBuffer().append("success=").append(success).append(", fail=").append(fail).toString());
        return success;
    }

    public void testRead() throws Exception {
        int total = 0;
        String[] logs = EventLog.getLogNames();
        for (int i = 0; i < logs.length; i++) {
            String msg = new StringBuffer().append("readAll(").append(logs[i]).append(")").toString();
            traceln(msg);
            total += readAll(logs[i]);
        }
        if (total == 0) {
            fail("No eventlog entries read");
        }
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/test/TestEventLog$SSHEventLogNotification.class */
    private class SSHEventLogNotification implements EventLogNotification {
        private final TestEventLog this$0;

        private SSHEventLogNotification(TestEventLog testEventLog) {
            this.this$0 = testEventLog;
        }

        SSHEventLogNotification(TestEventLog x0, AnonymousClass1 x1) {
            this(x0);
        }

        @Override // org.hyperic.sigar.win32.EventLogNotification
        public boolean matches(EventLogRecord record) {
            return record.getSource().equals("sshd");
        }

        @Override // org.hyperic.sigar.win32.EventLogNotification
        public void handleNotification(EventLogRecord record) {
            System.out.println(record);
        }
    }

    public void testEventLogThread() throws Exception {
        EventLogThread thread = EventLogThread.getInstance(EventLog.APPLICATION);
        thread.doStart();
        SSHEventLogNotification notification = new SSHEventLogNotification(this, null);
        thread.add(notification);
        thread.doStop();
    }
}
