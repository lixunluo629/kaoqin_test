package org.hyperic.sigar;

import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.tomcat.jni.Time;
import org.hyperic.sigar.jmx.CpuTimerMBean;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/CpuTimer.class */
public class CpuTimer implements CpuTimerMBean {
    private static final Map timers = Collections.synchronizedMap(new HashMap());
    private Sigar sigar;
    private long totalTime;
    private long cpuTotal;
    private long cpuUser;
    private long cpuSys;
    private long cpuSampleFirst;
    private long cpuSampleLast;
    private long cpuSampleTime;
    private ThreadCpu cpu;
    private long startTime;
    private long stopTime;

    public CpuTimer() {
        this(null);
    }

    public CpuTimer(Sigar sigar) {
        this.cpu = new ThreadCpu();
        clear();
        this.sigar = sigar;
    }

    public void clear() {
        this.startTime = -1L;
        this.stopTime = -1L;
        this.totalTime = 0L;
        this.cpuTotal = 0L;
        this.cpuUser = 0L;
        this.cpuSys = 0L;
        this.cpuSampleFirst = 0L;
        this.cpuSampleLast = 0L;
        this.cpuSampleTime = 0L;
    }

    private void stamp(CpuTimer timer) {
        if (this.cpuSampleFirst == 0) {
            this.cpuSampleFirst = toMillis(timer.cpu.total);
            this.cpuSampleTime = timer.startTime;
        } else {
            this.cpuSampleLast = toMillis(timer.cpu.total);
        }
    }

    public void add(CpuTimer timer) {
        stamp(timer);
        this.cpuTotal += timer.cpuTotal;
        this.cpuUser += timer.cpuUser;
        this.cpuSys += timer.cpuSys;
        this.totalTime += timer.totalTime;
    }

    public void start() {
        start(this.sigar);
    }

    public void start(Sigar sigar) {
        this.startTime = System.currentTimeMillis();
        try {
            this.cpu.gather(sigar, 0L);
            stamp(this);
        } catch (SigarException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    public void stop() {
        stop(this.sigar);
    }

    public void stop(Sigar sigar) {
        ThreadCpu diff = getDiff(sigar);
        this.cpuTotal += diff.total;
        this.cpuUser += diff.user;
        this.cpuSys += diff.sys;
        this.stopTime = System.currentTimeMillis();
        double timeDiff = this.stopTime - this.startTime;
        this.totalTime = (long) (this.totalTime + timeDiff);
    }

    public ThreadCpu getDiff() {
        return getDiff(this.sigar);
    }

    public ThreadCpu getDiff(Sigar sigar) {
        long startTotal = this.cpu.total;
        long startUser = this.cpu.user;
        long startSys = this.cpu.sys;
        ThreadCpu diff = new ThreadCpu();
        try {
            this.cpu.gather(sigar, 0L);
            diff.total = this.cpu.total - startTotal;
            diff.user = this.cpu.user - startUser;
            diff.sys = this.cpu.sys - startSys;
            stamp(this);
            return diff;
        } catch (SigarException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    @Override // org.hyperic.sigar.jmx.CpuTimerMBean
    public long getTotalTime() {
        return this.totalTime;
    }

    private long toMillis(long ns) {
        return ns / Time.APR_USEC_PER_SEC;
    }

    @Override // org.hyperic.sigar.jmx.CpuTimerMBean
    public long getCpuTotal() {
        return toMillis(this.cpuTotal);
    }

    @Override // org.hyperic.sigar.jmx.CpuTimerMBean
    public long getCpuUser() {
        return toMillis(this.cpuUser);
    }

    @Override // org.hyperic.sigar.jmx.CpuTimerMBean
    public long getCpuSys() {
        return toMillis(this.cpuSys);
    }

    @Override // org.hyperic.sigar.jmx.CpuTimerMBean
    public double getCpuUsage() {
        if (this.cpuSampleFirst == 0 || this.cpuSampleLast == 0) {
            return 0.0d;
        }
        long timeNow = System.currentTimeMillis();
        double diff = timeNow - this.cpuSampleTime;
        if (diff == 0.0d) {
            return 0.0d;
        }
        double usage = (this.cpuSampleLast - this.cpuSampleFirst) / diff;
        this.cpuSampleFirst = 0L;
        this.cpuSampleLast = 0L;
        this.cpuSampleTime = 0L;
        return usage;
    }

    @Override // org.hyperic.sigar.jmx.CpuTimerMBean
    public long getLastSampleTime() {
        return this.stopTime;
    }

    public static CpuTimer getInstance(String name) {
        CpuTimer timer = (CpuTimer) timers.get(name);
        if (timer == null) {
            timer = new CpuTimer();
            timers.put(name, timer);
        }
        return timer;
    }

    public String format(long elap) {
        String fraction = new StringBuffer().append(elap % 1000).append("").toString();
        int pad = 3 - fraction.length();
        StringBuffer buf = new StringBuffer().append(elap / 1000).append('.');
        while (true) {
            int i = pad;
            pad = i - 1;
            if (i > 0) {
                buf.append("0");
            } else {
                buf.append(fraction).append(" seconds");
                return buf.toString();
            }
        }
    }

    public void list(PrintStream out) {
        out.println(new StringBuffer().append("real.....").append(format(getTotalTime())).toString());
        out.println(new StringBuffer().append("user.....").append(format(getCpuUser())).toString());
        out.println(new StringBuffer().append("sys......").append(format(getCpuSys())).toString());
        out.println(new StringBuffer().append("usage....").append(CpuPerc.format(getCpuUsage())).toString());
    }
}
