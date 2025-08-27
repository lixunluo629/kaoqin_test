package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/DiskUsage.class */
public class DiskUsage implements Serializable {
    private static final long serialVersionUID = 8090;
    long reads = 0;
    long writes = 0;
    long readBytes = 0;
    long writeBytes = 0;
    double queue = 0.0d;
    double serviceTime = 0.0d;

    public native void gather(Sigar sigar, String str) throws SigarException;

    static DiskUsage fetch(Sigar sigar, String name) throws SigarException {
        DiskUsage diskUsage = new DiskUsage();
        diskUsage.gather(sigar, name);
        return diskUsage;
    }

    public long getReads() {
        return this.reads;
    }

    public long getWrites() {
        return this.writes;
    }

    public long getReadBytes() {
        return this.readBytes;
    }

    public long getWriteBytes() {
        return this.writeBytes;
    }

    public double getQueue() {
        return this.queue;
    }

    public double getServiceTime() {
        return this.serviceTime;
    }

    void copyTo(DiskUsage copy) {
        copy.reads = this.reads;
        copy.writes = this.writes;
        copy.readBytes = this.readBytes;
        copy.writeBytes = this.writeBytes;
        copy.queue = this.queue;
        copy.serviceTime = this.serviceTime;
    }

    public Map toMap() {
        Map map = new HashMap();
        String strreads = String.valueOf(this.reads);
        if (!"-1".equals(strreads)) {
            map.put("Reads", strreads);
        }
        String strwrites = String.valueOf(this.writes);
        if (!"-1".equals(strwrites)) {
            map.put("Writes", strwrites);
        }
        String strreadBytes = String.valueOf(this.readBytes);
        if (!"-1".equals(strreadBytes)) {
            map.put("ReadBytes", strreadBytes);
        }
        String strwriteBytes = String.valueOf(this.writeBytes);
        if (!"-1".equals(strwriteBytes)) {
            map.put("WriteBytes", strwriteBytes);
        }
        String strqueue = String.valueOf(this.queue);
        if (!"-1".equals(strqueue)) {
            map.put("Queue", strqueue);
        }
        String strserviceTime = String.valueOf(this.serviceTime);
        if (!"-1".equals(strserviceTime)) {
            map.put("ServiceTime", strserviceTime);
        }
        return map;
    }

    public String toString() {
        return toMap().toString();
    }
}
