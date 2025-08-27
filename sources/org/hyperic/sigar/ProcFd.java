package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/ProcFd.class */
public class ProcFd implements Serializable {
    private static final long serialVersionUID = 948;
    long total = 0;

    public native void gather(Sigar sigar, long j) throws SigarException;

    static ProcFd fetch(Sigar sigar, long pid) throws SigarException {
        ProcFd procFd = new ProcFd();
        procFd.gather(sigar, pid);
        return procFd;
    }

    public long getTotal() {
        return this.total;
    }

    void copyTo(ProcFd copy) {
        copy.total = this.total;
    }

    public Map toMap() {
        Map map = new HashMap();
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
