package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/ProcMem.class */
public class ProcMem implements Serializable {
    private static final long serialVersionUID = 7985;
    long size = 0;
    long resident = 0;
    long share = 0;
    long minorFaults = 0;
    long majorFaults = 0;
    long pageFaults = 0;

    public native void gather(Sigar sigar, long j) throws SigarException;

    static ProcMem fetch(Sigar sigar, long pid) throws SigarException {
        ProcMem procMem = new ProcMem();
        procMem.gather(sigar, pid);
        return procMem;
    }

    public long getSize() {
        return this.size;
    }

    public long getResident() {
        return this.resident;
    }

    public long getShare() {
        return this.share;
    }

    public long getMinorFaults() {
        return this.minorFaults;
    }

    public long getMajorFaults() {
        return this.majorFaults;
    }

    public long getPageFaults() {
        return this.pageFaults;
    }

    void copyTo(ProcMem copy) {
        copy.size = this.size;
        copy.resident = this.resident;
        copy.share = this.share;
        copy.minorFaults = this.minorFaults;
        copy.majorFaults = this.majorFaults;
        copy.pageFaults = this.pageFaults;
    }

    public long getRss() {
        return getResident();
    }

    public long getVsize() {
        return getSize();
    }

    public Map toMap() {
        Map map = new HashMap();
        String strsize = String.valueOf(this.size);
        if (!"-1".equals(strsize)) {
            map.put("Size", strsize);
        }
        String strresident = String.valueOf(this.resident);
        if (!"-1".equals(strresident)) {
            map.put("Resident", strresident);
        }
        String strshare = String.valueOf(this.share);
        if (!"-1".equals(strshare)) {
            map.put("Share", strshare);
        }
        String strminorFaults = String.valueOf(this.minorFaults);
        if (!"-1".equals(strminorFaults)) {
            map.put("MinorFaults", strminorFaults);
        }
        String strmajorFaults = String.valueOf(this.majorFaults);
        if (!"-1".equals(strmajorFaults)) {
            map.put("MajorFaults", strmajorFaults);
        }
        String strpageFaults = String.valueOf(this.pageFaults);
        if (!"-1".equals(strpageFaults)) {
            map.put("PageFaults", strpageFaults);
        }
        return map;
    }

    public String toString() {
        return toMap().toString();
    }
}
