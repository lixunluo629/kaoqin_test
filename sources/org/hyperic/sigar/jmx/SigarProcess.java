package org.hyperic.sigar.jmx;

import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.ProcFd;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/jmx/SigarProcess.class */
public class SigarProcess implements SigarProcessMBean {
    private Sigar sigarImpl;
    private SigarProxy sigar;

    public SigarProcess() {
        this(new Sigar());
    }

    public SigarProcess(Sigar sigar) {
        this.sigarImpl = sigar;
        this.sigar = SigarProxyCache.newInstance(this.sigarImpl);
    }

    public void close() {
        this.sigarImpl.close();
    }

    private RuntimeException unexpectedError(String type, SigarException e) {
        String msg = new StringBuffer().append("Unexected error in Sigar.get").append(type).append(": ").append(e.getMessage()).toString();
        return new IllegalArgumentException(msg);
    }

    private synchronized ProcMem getMem() {
        try {
            long pid = this.sigar.getPid();
            return this.sigar.getProcMem(pid);
        } catch (SigarException e) {
            throw unexpectedError("Mem", e);
        }
    }

    private synchronized ProcCpu getCpu() {
        try {
            long pid = this.sigar.getPid();
            return this.sigar.getProcCpu(pid);
        } catch (SigarException e) {
            throw unexpectedError("Cpu", e);
        }
    }

    private synchronized ProcFd getFd() {
        try {
            long pid = this.sigar.getPid();
            return this.sigar.getProcFd(pid);
        } catch (SigarException e) {
            throw unexpectedError("Fd", e);
        }
    }

    @Override // org.hyperic.sigar.jmx.SigarProcessMBean
    public Long getMemSize() {
        return new Long(getMem().getSize());
    }

    @Override // org.hyperic.sigar.jmx.SigarProcessMBean
    public Long getMemVsize() {
        return getMemSize();
    }

    @Override // org.hyperic.sigar.jmx.SigarProcessMBean
    public Long getMemResident() {
        return new Long(getMem().getResident());
    }

    @Override // org.hyperic.sigar.jmx.SigarProcessMBean
    public Long getMemShare() {
        return new Long(getMem().getShare());
    }

    @Override // org.hyperic.sigar.jmx.SigarProcessMBean
    public Long getMemPageFaults() {
        return new Long(getMem().getPageFaults());
    }

    @Override // org.hyperic.sigar.jmx.SigarProcessMBean
    public Long getTimeUser() {
        return new Long(getCpu().getUser());
    }

    @Override // org.hyperic.sigar.jmx.SigarProcessMBean
    public Long getTimeSys() {
        return new Long(getCpu().getSys());
    }

    @Override // org.hyperic.sigar.jmx.SigarProcessMBean
    public Double getCpuUsage() {
        return new Double(getCpu().getPercent());
    }

    @Override // org.hyperic.sigar.jmx.SigarProcessMBean
    public Long getOpenFd() {
        return new Long(getFd().getTotal());
    }

    public static void main(String[] args) {
        SigarProcessMBean proc = new SigarProcess();
        System.out.println(new StringBuffer().append("MemSize=").append(proc.getMemSize()).toString());
        System.out.println(new StringBuffer().append("MemResident=").append(proc.getMemResident()).toString());
        System.out.println(new StringBuffer().append("MemShared=").append(proc.getMemShare()).toString());
        System.out.println(new StringBuffer().append("MemPageFaults=").append(proc.getMemPageFaults()).toString());
        System.out.println(new StringBuffer().append("TimeUser=").append(proc.getTimeUser()).toString());
        System.out.println(new StringBuffer().append("TimeSys=").append(proc.getTimeSys()).toString());
        System.out.println(new StringBuffer().append("OpenFd=").append(proc.getOpenFd()).toString());
    }
}
