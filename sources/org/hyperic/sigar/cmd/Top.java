package org.hyperic.sigar.cmd;

import java.util.List;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.ProcStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Top.class */
public class Top {
    private static final int SLEEP_TIME = 5000;
    private static final String HEADER = "PID\tUSER\tSTIME\tSIZE\tRSS\tSHARE\tSTATE\tTIME\t%CPU\tCOMMAND";

    private static String toString(ProcStat stat) {
        return new StringBuffer().append(stat.getTotal()).append(" processes: ").append(stat.getSleeping()).append(" sleeping, ").append(stat.getRunning()).append(" running, ").append(stat.getZombie()).append(" zombie, ").append(stat.getStopped()).append(" stopped... ").append(stat.getThreads()).append(" threads").toString();
    }

    public static void main(String[] args) throws Exception {
        Sigar sigarImpl = new Sigar();
        SigarProxy sigar = SigarProxyCache.newInstance(sigarImpl, 5000);
        while (true) {
            Shell.clearScreen();
            System.out.println(Uptime.getInfo(sigar));
            System.out.println(toString(sigar.getProcStat()));
            System.out.println(sigar.getCpuPerc());
            System.out.println(sigar.getMem());
            System.out.println(sigar.getSwap());
            System.out.println();
            System.out.println(HEADER);
            long[] pids = Shell.getPids(sigar, args);
            for (long pid : pids) {
                String cpuPerc = "?";
                try {
                    List info = Ps.getInfo(sigar, pid);
                    try {
                        ProcCpu cpu = sigar.getProcCpu(pid);
                        cpuPerc = CpuPerc.format(cpu.getPercent());
                    } catch (SigarException e) {
                    }
                    info.add(info.size() - 1, cpuPerc);
                    System.out.println(Ps.join(info));
                } catch (SigarException e2) {
                }
            }
            Thread.sleep(5000L);
            SigarProxyCache.clear(sigar);
        }
    }
}
