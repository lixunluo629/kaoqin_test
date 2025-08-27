package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/FileSystemUsage.class */
public class FileSystemUsage implements Serializable {
    private static final long serialVersionUID = 18905;
    long total = 0;
    long free = 0;
    long used = 0;
    long avail = 0;
    long files = 0;
    long freeFiles = 0;
    long diskReads = 0;
    long diskWrites = 0;
    long diskReadBytes = 0;
    long diskWriteBytes = 0;
    double diskQueue = 0.0d;
    double diskServiceTime = 0.0d;
    double usePercent = 0.0d;

    public native void gather(Sigar sigar, String str) throws SigarException;

    static FileSystemUsage fetch(Sigar sigar, String name) throws SigarException {
        FileSystemUsage fileSystemUsage = new FileSystemUsage();
        fileSystemUsage.gather(sigar, name);
        return fileSystemUsage;
    }

    public long getTotal() {
        return this.total;
    }

    public long getFree() {
        return this.free;
    }

    public long getUsed() {
        return this.used;
    }

    public long getAvail() {
        return this.avail;
    }

    public long getFiles() {
        return this.files;
    }

    public long getFreeFiles() {
        return this.freeFiles;
    }

    public long getDiskReads() {
        return this.diskReads;
    }

    public long getDiskWrites() {
        return this.diskWrites;
    }

    public long getDiskReadBytes() {
        return this.diskReadBytes;
    }

    public long getDiskWriteBytes() {
        return this.diskWriteBytes;
    }

    public double getDiskQueue() {
        return this.diskQueue;
    }

    public double getDiskServiceTime() {
        return this.diskServiceTime;
    }

    public double getUsePercent() {
        return this.usePercent;
    }

    void copyTo(FileSystemUsage copy) {
        copy.total = this.total;
        copy.free = this.free;
        copy.used = this.used;
        copy.avail = this.avail;
        copy.files = this.files;
        copy.freeFiles = this.freeFiles;
        copy.diskReads = this.diskReads;
        copy.diskWrites = this.diskWrites;
        copy.diskReadBytes = this.diskReadBytes;
        copy.diskWriteBytes = this.diskWriteBytes;
        copy.diskQueue = this.diskQueue;
        copy.diskServiceTime = this.diskServiceTime;
        copy.usePercent = this.usePercent;
    }

    public Map toMap() {
        Map map = new HashMap();
        String strtotal = String.valueOf(this.total);
        if (!"-1".equals(strtotal)) {
            map.put("Total", strtotal);
        }
        String strfree = String.valueOf(this.free);
        if (!"-1".equals(strfree)) {
            map.put("Free", strfree);
        }
        String strused = String.valueOf(this.used);
        if (!"-1".equals(strused)) {
            map.put("Used", strused);
        }
        String stravail = String.valueOf(this.avail);
        if (!"-1".equals(stravail)) {
            map.put("Avail", stravail);
        }
        String strfiles = String.valueOf(this.files);
        if (!"-1".equals(strfiles)) {
            map.put("Files", strfiles);
        }
        String strfreeFiles = String.valueOf(this.freeFiles);
        if (!"-1".equals(strfreeFiles)) {
            map.put("FreeFiles", strfreeFiles);
        }
        String strdiskReads = String.valueOf(this.diskReads);
        if (!"-1".equals(strdiskReads)) {
            map.put("DiskReads", strdiskReads);
        }
        String strdiskWrites = String.valueOf(this.diskWrites);
        if (!"-1".equals(strdiskWrites)) {
            map.put("DiskWrites", strdiskWrites);
        }
        String strdiskReadBytes = String.valueOf(this.diskReadBytes);
        if (!"-1".equals(strdiskReadBytes)) {
            map.put("DiskReadBytes", strdiskReadBytes);
        }
        String strdiskWriteBytes = String.valueOf(this.diskWriteBytes);
        if (!"-1".equals(strdiskWriteBytes)) {
            map.put("DiskWriteBytes", strdiskWriteBytes);
        }
        String strdiskQueue = String.valueOf(this.diskQueue);
        if (!"-1".equals(strdiskQueue)) {
            map.put("DiskQueue", strdiskQueue);
        }
        String strdiskServiceTime = String.valueOf(this.diskServiceTime);
        if (!"-1".equals(strdiskServiceTime)) {
            map.put("DiskServiceTime", strdiskServiceTime);
        }
        String strusePercent = String.valueOf(this.usePercent);
        if (!"-1".equals(strusePercent)) {
            map.put("UsePercent", strusePercent);
        }
        return map;
    }

    public String toString() {
        return toMap().toString();
    }
}
