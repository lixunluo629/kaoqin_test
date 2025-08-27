package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/Swap.class */
public class Swap implements Serializable {
    private static final long serialVersionUID = 4974;
    long total = 0;
    long used = 0;
    long free = 0;
    long pageIn = 0;
    long pageOut = 0;

    public native void gather(Sigar sigar) throws SigarException;

    static Swap fetch(Sigar sigar) throws SigarException {
        Swap swap = new Swap();
        swap.gather(sigar);
        return swap;
    }

    public long getTotal() {
        return this.total;
    }

    public long getUsed() {
        return this.used;
    }

    public long getFree() {
        return this.free;
    }

    public long getPageIn() {
        return this.pageIn;
    }

    public long getPageOut() {
        return this.pageOut;
    }

    void copyTo(Swap copy) {
        copy.total = this.total;
        copy.used = this.used;
        copy.free = this.free;
        copy.pageIn = this.pageIn;
        copy.pageOut = this.pageOut;
    }

    public String toString() {
        return new StringBuffer().append("Swap: ").append(this.total / 1024).append("K av, ").append(this.used / 1024).append("K used, ").append(this.free / 1024).append("K free").toString();
    }

    public Map toMap() {
        Map map = new HashMap();
        String strtotal = String.valueOf(this.total);
        if (!"-1".equals(strtotal)) {
            map.put("Total", strtotal);
        }
        String strused = String.valueOf(this.used);
        if (!"-1".equals(strused)) {
            map.put("Used", strused);
        }
        String strfree = String.valueOf(this.free);
        if (!"-1".equals(strfree)) {
            map.put("Free", strfree);
        }
        String strpageIn = String.valueOf(this.pageIn);
        if (!"-1".equals(strpageIn)) {
            map.put("PageIn", strpageIn);
        }
        String strpageOut = String.valueOf(this.pageOut);
        if (!"-1".equals(strpageOut)) {
            map.put("PageOut", strpageOut);
        }
        return map;
    }
}
