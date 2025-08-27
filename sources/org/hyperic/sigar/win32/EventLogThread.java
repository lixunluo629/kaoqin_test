package org.hyperic.sigar.win32;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/EventLogThread.class */
public class EventLogThread implements Runnable {
    public static final int DEFAULT_INTERVAL = 60000;
    private static Logger logger;
    private static HashMap logs;
    static Class class$org$hyperic$sigar$win32$EventLogThread;
    private Thread thread = null;
    private boolean shouldDie = false;
    private Set notifiers = Collections.synchronizedSet(new HashSet());
    private String logName = EventLog.APPLICATION;
    private long interval = 60000;

    static {
        Class clsClass$;
        if (class$org$hyperic$sigar$win32$EventLogThread == null) {
            clsClass$ = class$("org.hyperic.sigar.win32.EventLogThread");
            class$org$hyperic$sigar$win32$EventLogThread = clsClass$;
        } else {
            clsClass$ = class$org$hyperic$sigar$win32$EventLogThread;
        }
        logger = Logger.getLogger(clsClass$.getName());
        logs = new HashMap();
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public static EventLogThread getInstance() {
        return getInstance(EventLog.APPLICATION);
    }

    public static EventLogThread getInstance(String name) {
        EventLogThread instance;
        synchronized (logs) {
            instance = (EventLogThread) logs.get(name);
            if (instance == null) {
                instance = new EventLogThread();
                instance.setLogName(name);
                logs.put(name, instance);
            }
        }
        return instance;
    }

    public static void closeInstances() {
        synchronized (logs) {
            for (EventLogThread eventLogThread : logs.values()) {
                eventLogThread.doStop();
            }
            logs.clear();
        }
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public synchronized void doStart() {
        if (this.thread != null) {
            return;
        }
        this.thread = new Thread(this, "EventLogThread");
        this.thread.setDaemon(true);
        this.thread.start();
        logger.debug(new StringBuffer().append(this.thread.getName()).append(" started").toString());
    }

    public synchronized void doStop() {
        if (this.thread == null) {
            return;
        }
        die();
        this.thread.interrupt();
        logger.debug(new StringBuffer().append(this.thread.getName()).append(" stopped").toString());
        this.thread = null;
    }

    public void add(EventLogNotification notifier) {
        this.notifiers.add(notifier);
    }

    public void remove(EventLogNotification notifier) {
        this.notifiers.remove(notifier);
    }

    private void handleEvents(EventLog log, int curEvent, int lastEvent) {
        for (int i = curEvent + 1; i <= lastEvent; i++) {
            try {
                EventLogRecord record = log.read(i);
                synchronized (this.notifiers) {
                    for (EventLogNotification notification : this.notifiers) {
                        if (notification.matches(record)) {
                            notification.handleNotification(record);
                        }
                    }
                }
            } catch (Win32Exception e) {
                logger.error(new StringBuffer().append("Unable to read event id ").append(i).append(": ").append(e).toString());
            }
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        EventLog log = new EventLog();
        try {
            try {
                log.open(this.logName);
                int curEvent = log.getNewestRecord();
                while (!this.shouldDie) {
                    int lastEvent = log.getNewestRecord();
                    if (lastEvent > curEvent) {
                        handleEvents(log, curEvent, lastEvent);
                    }
                    curEvent = lastEvent;
                    try {
                        Thread.sleep(this.interval);
                    } catch (InterruptedException e) {
                    }
                }
            } finally {
                try {
                    log.close();
                } catch (Win32Exception e2) {
                }
            }
        } catch (Win32Exception e3) {
            logger.error("Unable to monitor event log: ", e3);
            try {
                log.close();
            } catch (Win32Exception e4) {
            }
        }
    }

    public void die() {
        this.shouldDie = true;
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            args = new String[]{EventLog.SYSTEM, EventLog.APPLICATION, EventLog.SECURITY};
        }
        EventLogNotification watcher = new EventLogNotification() { // from class: org.hyperic.sigar.win32.EventLogThread.1
            @Override // org.hyperic.sigar.win32.EventLogNotification
            public boolean matches(EventLogRecord record) {
                return true;
            }

            @Override // org.hyperic.sigar.win32.EventLogNotification
            public void handleNotification(EventLogRecord record) {
                System.out.println(record);
            }
        };
        for (String name : args) {
            EventLogThread eventLogThread = getInstance(name);
            eventLogThread.doStart();
            eventLogThread.setInterval(1000L);
            eventLogThread.add(watcher);
        }
        System.out.println("Press any key to stop");
        try {
            System.in.read();
        } catch (IOException e) {
        }
        closeInstances();
    }
}
