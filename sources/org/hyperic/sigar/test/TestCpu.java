package org.hyperic.sigar.test;

import ch.qos.logback.classic.net.SyslogAppender;
import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;
import org.springframework.util.ClassUtils;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestCpu.class */
public class TestCpu extends SigarTestCase {
    public TestCpu(String name) {
        super(name);
    }

    private void checkCpu(Cpu cpu) {
        traceln(new StringBuffer().append("User...").append(cpu.getUser()).toString());
        assertTrue(cpu.getUser() >= 0);
        traceln(new StringBuffer().append("Sys....").append(cpu.getSys()).toString());
        assertTrue(cpu.getSys() >= 0);
        traceln(new StringBuffer().append("Idle...").append(cpu.getIdle()).toString());
        assertTrue(cpu.getIdle() >= 0);
        traceln(new StringBuffer().append("Wait...").append(cpu.getWait()).toString());
        assertTrue(cpu.getWait() >= 0);
        traceln(new StringBuffer().append("Irq...").append(cpu.getIrq()).toString());
        assertTrue(cpu.getIrq() >= 0);
        traceln(new StringBuffer().append("SIrq..").append(cpu.getSoftIrq()).toString());
        assertTrue(cpu.getSoftIrq() >= 0);
        traceln(new StringBuffer().append("Stl...").append(cpu.getStolen()).toString());
        assertTrue(cpu.getStolen() >= 0);
        traceln(new StringBuffer().append("Total..").append(cpu.getTotal()).toString());
        assertTrue(cpu.getTotal() > 0);
        try {
            long current = getSigar().getProcState(ClassUtils.CGLIB_CLASS_SEPARATOR).getProcessor();
            traceln(new StringBuffer().append("last run cpu=").append(current).toString());
        } catch (SigarException e) {
            e.printStackTrace();
        }
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        Cpu cpu = sigar.getCpu();
        traceln("getCpu:");
        checkCpu(cpu);
        try {
            Cpu[] cpuList = sigar.getCpuList();
            for (int i = 0; i < cpuList.length; i++) {
                traceln(new StringBuffer().append("Cpu ").append(i).append(":").toString());
                checkCpu(cpuList[i]);
            }
        } catch (SigarNotImplementedException e) {
        }
    }

    private static void printCpu(String prefix, CpuPerc cpu) {
        System.out.println(new StringBuffer().append(prefix).append(CpuPerc.format(cpu.getUser())).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(CpuPerc.format(cpu.getSys())).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(CpuPerc.format(cpu.getWait())).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(CpuPerc.format(cpu.getNice())).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(CpuPerc.format(cpu.getIdle())).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(CpuPerc.format(cpu.getCombined())).toString());
    }

    public static void main(String[] args) throws Exception {
        int interval = 1;
        if (args.length > 0) {
            interval = Integer.parseInt(args[0]);
        }
        int sleep = 60000 * interval;
        Sigar sigar = new Sigar();
        while (true) {
            System.out.println("   User\tSys\tWait\tNice\tIdle\tTotal");
            printCpu("   ", sigar.getCpuPerc());
            CpuPerc[] cpuList = sigar.getCpuPercList();
            for (int i = 0; i < cpuList.length; i++) {
                printCpu(new StringBuffer().append(i).append(": ").toString(), cpuList[i]);
            }
            Thread.sleep(sleep);
        }
    }
}
