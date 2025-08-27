package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import org.hyperic.sigar.ptql.ProcessFinder;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/MultiProcCpu.class */
public class MultiProcCpu extends ProcCpu {
    private long pid;
    private int nproc = 0;
    private static Map ptable = new HashMap();

    static synchronized MultiProcCpu get(Sigar sigar, String query) throws SigarException {
        MultiProcCpu cpu = (MultiProcCpu) ptable.get(query);
        if (cpu == null) {
            cpu = new MultiProcCpu();
            cpu.pid = query.hashCode();
            ptable.put(query, cpu);
        }
        long timeNow = System.currentTimeMillis();
        double diff = timeNow - cpu.lastTime;
        if (diff == 0.0d) {
            return cpu;
        }
        cpu.lastTime = timeNow;
        long otime = cpu.total;
        cpu.total = 0L;
        cpu.user = 0L;
        cpu.sys = 0L;
        cpu.nproc = 0;
        long[] pids = ProcessFinder.find(sigar, query);
        cpu.nproc = pids.length;
        for (long j : pids) {
            try {
                ProcTime time = sigar.getProcTime(j);
                cpu.total += time.total;
                cpu.user += time.user;
                cpu.sys += time.sys;
            } catch (SigarException e) {
            }
        }
        if (otime == 0) {
            return cpu;
        }
        cpu.percent = (cpu.total - otime) / diff;
        if (cpu.percent < 0.0d) {
            cpu.percent = 0.0d - cpu.percent;
        }
        if (cpu.percent >= 1.0d) {
            cpu.percent = 0.99d;
        }
        return cpu;
    }

    @Override // org.hyperic.sigar.ProcCpu
    public double getPercent() {
        return this.percent;
    }

    public int getProcesses() {
        return this.nproc;
    }

    public int hashCode() {
        return (int) this.pid;
    }

    public boolean equals(Object cpu) {
        return (cpu instanceof MultiProcCpu) && ((MultiProcCpu) cpu).pid == this.pid;
    }
}
