package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/CpuInfo.class */
public class CpuInfo implements Serializable {
    private static final long serialVersionUID = 9710;
    String vendor = null;
    String model = null;
    int mhz = 0;
    long cacheSize = 0;
    int totalCores = 0;
    int totalSockets = 0;
    int coresPerSocket = 0;

    public native void gather(Sigar sigar) throws SigarException;

    static CpuInfo fetch(Sigar sigar) throws SigarException {
        CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.gather(sigar);
        return cpuInfo;
    }

    public String getVendor() {
        return this.vendor;
    }

    public String getModel() {
        return this.model;
    }

    public int getMhz() {
        return this.mhz;
    }

    public long getCacheSize() {
        return this.cacheSize;
    }

    public int getTotalCores() {
        return this.totalCores;
    }

    public int getTotalSockets() {
        return this.totalSockets;
    }

    public int getCoresPerSocket() {
        return this.coresPerSocket;
    }

    void copyTo(CpuInfo copy) {
        copy.vendor = this.vendor;
        copy.model = this.model;
        copy.mhz = this.mhz;
        copy.cacheSize = this.cacheSize;
        copy.totalCores = this.totalCores;
        copy.totalSockets = this.totalSockets;
        copy.coresPerSocket = this.coresPerSocket;
    }

    public Map toMap() {
        Map map = new HashMap();
        String strvendor = String.valueOf(this.vendor);
        if (!"-1".equals(strvendor)) {
            map.put("Vendor", strvendor);
        }
        String strmodel = String.valueOf(this.model);
        if (!"-1".equals(strmodel)) {
            map.put("Model", strmodel);
        }
        String strmhz = String.valueOf(this.mhz);
        if (!"-1".equals(strmhz)) {
            map.put("Mhz", strmhz);
        }
        String strcacheSize = String.valueOf(this.cacheSize);
        if (!"-1".equals(strcacheSize)) {
            map.put("CacheSize", strcacheSize);
        }
        String strtotalCores = String.valueOf(this.totalCores);
        if (!"-1".equals(strtotalCores)) {
            map.put("TotalCores", strtotalCores);
        }
        String strtotalSockets = String.valueOf(this.totalSockets);
        if (!"-1".equals(strtotalSockets)) {
            map.put("TotalSockets", strtotalSockets);
        }
        String strcoresPerSocket = String.valueOf(this.coresPerSocket);
        if (!"-1".equals(strcoresPerSocket)) {
            map.put("CoresPerSocket", strcoresPerSocket);
        }
        return map;
    }

    public String toString() {
        return toMap().toString();
    }
}
