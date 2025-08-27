package org.hyperic.sigar.jmx;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/jmx/CpuTimerMBean.class */
public interface CpuTimerMBean {
    long getCpuTotal();

    long getCpuUser();

    long getCpuSys();

    double getCpuUsage();

    long getTotalTime();

    long getLastSampleTime();
}
