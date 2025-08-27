package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/ProcTime.class */
public class ProcTime implements Serializable {
    private static final long serialVersionUID = 4030;
    long startTime = 0;
    long user = 0;
    long sys = 0;
    long total = 0;

    public native void gather(Sigar sigar, long j) throws SigarException;

    static ProcTime fetch(Sigar sigar, long pid) throws SigarException {
        ProcTime procTime = new ProcTime();
        procTime.gather(sigar, pid);
        return procTime;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getUser() {
        return this.user;
    }

    public long getSys() {
        return this.sys;
    }

    public long getTotal() {
        return this.total;
    }

    void copyTo(ProcTime copy) {
        copy.startTime = this.startTime;
        copy.user = this.user;
        copy.sys = this.sys;
        copy.total = this.total;
    }

    public Map toMap() {
        Map map = new HashMap();
        String strstartTime = String.valueOf(this.startTime);
        if (!"-1".equals(strstartTime)) {
            map.put("StartTime", strstartTime);
        }
        String struser = String.valueOf(this.user);
        if (!"-1".equals(struser)) {
            map.put("User", struser);
        }
        String strsys = String.valueOf(this.sys);
        if (!"-1".equals(strsys)) {
            map.put("Sys", strsys);
        }
        String strtotal = String.valueOf(this.total);
        if (!"-1".equals(strtotal)) {
            map.put("Total", strtotal);
        }
        return map;
    }

    public String toString() {
        return toMap().toString();
    }
}
