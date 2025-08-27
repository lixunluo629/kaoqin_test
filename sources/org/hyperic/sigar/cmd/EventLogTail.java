package org.hyperic.sigar.cmd;

import org.hyperic.sigar.win32.EventLog;
import org.hyperic.sigar.win32.EventLogNotification;
import org.hyperic.sigar.win32.EventLogRecord;
import org.hyperic.sigar.win32.EventLogThread;
import org.hyperic.sigar.win32.Win32Exception;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/EventLogTail.class */
public class EventLogTail {

    /* renamed from: org.hyperic.sigar.cmd.EventLogTail$1, reason: invalid class name */
    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/EventLogTail$1.class */
    static class AnonymousClass1 {
    }

    private static void tail(String name, Tail tail) throws Win32Exception {
        EventLog log = new EventLog();
        log.open(name);
        int max = log.getNumberOfRecords();
        if (tail.number < max) {
            max = tail.number;
        }
        int last = log.getNewestRecord() + 1;
        int first = last - max;
        for (int i = first; i < last; i++) {
            EventLogRecord record = log.read(i);
            System.out.println(record);
        }
        log.close();
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/EventLogTail$TailNotification.class */
    private static class TailNotification implements EventLogNotification {
        private TailNotification() {
        }

        TailNotification(AnonymousClass1 x0) {
            this();
        }

        @Override // org.hyperic.sigar.win32.EventLogNotification
        public void handleNotification(EventLogRecord event) {
            System.out.println(event);
        }

        @Override // org.hyperic.sigar.win32.EventLogNotification
        public boolean matches(EventLogRecord event) {
            return true;
        }
    }

    public static void main(String[] args) throws Exception {
        Tail tail = new Tail();
        tail.parseArgs(args);
        if (tail.files.size() == 0) {
            tail.files.add(EventLog.SYSTEM);
        }
        for (int i = 0; i < tail.files.size(); i++) {
            String name = (String) tail.files.get(i);
            tail(name, tail);
            if (tail.follow) {
                TailNotification notifier = new TailNotification(null);
                EventLogThread thread = EventLogThread.getInstance(name);
                thread.add(notifier);
                thread.doStart();
            }
        }
        if (tail.follow) {
            System.in.read();
        }
    }
}
