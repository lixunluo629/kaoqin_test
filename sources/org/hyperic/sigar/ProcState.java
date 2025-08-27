package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/ProcState.class */
public class ProcState implements Serializable {
    private static final long serialVersionUID = 7805;
    char state = 0;
    String name = null;
    long ppid = 0;
    int tty = 0;
    int nice = 0;
    int priority = 0;
    long threads = 0;
    int processor = 0;
    public static final char SLEEP = 'S';
    public static final char RUN = 'R';
    public static final char STOP = 'T';
    public static final char ZOMBIE = 'Z';
    public static final char IDLE = 'D';

    public native void gather(Sigar sigar, long j) throws SigarException;

    static ProcState fetch(Sigar sigar, long pid) throws SigarException {
        ProcState procState = new ProcState();
        procState.gather(sigar, pid);
        return procState;
    }

    public char getState() {
        return this.state;
    }

    public String getName() {
        return this.name;
    }

    public long getPpid() {
        return this.ppid;
    }

    public int getTty() {
        return this.tty;
    }

    public int getNice() {
        return this.nice;
    }

    public int getPriority() {
        return this.priority;
    }

    public long getThreads() {
        return this.threads;
    }

    public int getProcessor() {
        return this.processor;
    }

    void copyTo(ProcState copy) {
        copy.state = this.state;
        copy.name = this.name;
        copy.ppid = this.ppid;
        copy.tty = this.tty;
        copy.nice = this.nice;
        copy.priority = this.priority;
        copy.threads = this.threads;
        copy.processor = this.processor;
    }

    public Map toMap() {
        Map map = new HashMap();
        String strstate = String.valueOf(this.state);
        if (!"-1".equals(strstate)) {
            map.put("State", strstate);
        }
        String strname = String.valueOf(this.name);
        if (!"-1".equals(strname)) {
            map.put("Name", strname);
        }
        String strppid = String.valueOf(this.ppid);
        if (!"-1".equals(strppid)) {
            map.put("Ppid", strppid);
        }
        String strtty = String.valueOf(this.tty);
        if (!"-1".equals(strtty)) {
            map.put("Tty", strtty);
        }
        String strnice = String.valueOf(this.nice);
        if (!"-1".equals(strnice)) {
            map.put("Nice", strnice);
        }
        String strpriority = String.valueOf(this.priority);
        if (!"-1".equals(strpriority)) {
            map.put("Priority", strpriority);
        }
        String strthreads = String.valueOf(this.threads);
        if (!"-1".equals(strthreads)) {
            map.put("Threads", strthreads);
        }
        String strprocessor = String.valueOf(this.processor);
        if (!"-1".equals(strprocessor)) {
            map.put("Processor", strprocessor);
        }
        return map;
    }

    public String toString() {
        return toMap().toString();
    }
}
