package org.hyperic.sigar.jmx;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/jmx/SigarProcessMBean.class */
public interface SigarProcessMBean {
    Long getMemSize();

    Long getMemVsize();

    Long getMemResident();

    Long getMemShare();

    Long getMemPageFaults();

    Long getTimeUser();

    Long getTimeSys();

    Double getCpuUsage();

    Long getOpenFd();
}
