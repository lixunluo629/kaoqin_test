package org.hyperic.sigar;

import java.io.Serializable;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/CpuPerc.class */
public class CpuPerc implements Serializable {
    private static final long serialVersionUID = 1393671;
    private double user;
    private double sys;
    private double nice;
    private double idle;
    private double wait;
    private double irq;
    private double softIrq;
    private double stolen;
    private double combined;

    native void gather(Sigar sigar, Cpu cpu, Cpu cpu2);

    CpuPerc() {
    }

    static CpuPerc fetch(Sigar sigar, Cpu oldCpu, Cpu curCpu) {
        CpuPerc perc = new CpuPerc();
        perc.gather(sigar, oldCpu, curCpu);
        return perc;
    }

    public static CpuPerc calculate(Cpu oldCpu, Cpu curCpu) {
        Sigar sigar = new Sigar();
        try {
            CpuPerc cpuPercFetch = fetch(sigar, oldCpu, curCpu);
            sigar.close();
            return cpuPercFetch;
        } catch (Throwable th) {
            sigar.close();
            throw th;
        }
    }

    public double getUser() {
        return this.user;
    }

    public double getSys() {
        return this.sys;
    }

    public double getNice() {
        return this.nice;
    }

    public double getIdle() {
        return this.idle;
    }

    public double getWait() {
        return this.wait;
    }

    public double getIrq() {
        return this.irq;
    }

    public double getSoftIrq() {
        return this.softIrq;
    }

    public double getStolen() {
        return this.stolen;
    }

    public double getCombined() {
        return this.combined;
    }

    public static String format(double val) {
        String p = String.valueOf(val * 100.0d);
        int ix = p.indexOf(".") + 1;
        String percent = new StringBuffer().append(p.substring(0, ix)).append(p.substring(ix, ix + 1)).toString();
        return new StringBuffer().append(percent).append(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL).toString();
    }

    public String toString() {
        return new StringBuffer().append("CPU states: ").append(format(this.user)).append(" user, ").append(format(this.sys)).append(" system, ").append(format(this.nice)).append(" nice, ").append(format(this.wait)).append(" wait, ").append(format(this.idle)).append(" idle").toString();
    }
}
