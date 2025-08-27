package org.hyperic.sigar.test;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.CpuTimer;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.ThreadCpu;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestThreadCpu.class */
public class TestThreadCpu extends SigarTestCase {
    public TestThreadCpu(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        try {
            ThreadCpu cpu = sigar.getThreadCpu();
            assertGtEqZeroTrace("User", cpu.getUser());
            assertGtEqZeroTrace("Sys", cpu.getSys());
            assertGtEqZeroTrace("Total", cpu.getTotal());
            CpuTimer timer = new CpuTimer(sigar);
            timer.start();
            for (int i = 0; i < 1000000; i++) {
                System.getProperty("java.home");
            }
            String sleepTime = System.getProperty("sigar.testThreadCpu.sleep");
            if (sleepTime != null) {
                Thread.sleep(Integer.parseInt(sleepTime) * 1000);
            }
            timer.stop();
            traceln("\nUsage...\n");
            assertGtEqZeroTrace("User", timer.getCpuUser());
            assertGtEqZeroTrace("Sys", timer.getCpuSys());
            assertGtEqZeroTrace("Total", timer.getCpuTotal());
            assertGtEqZeroTrace("Real Time", timer.getTotalTime());
            traceln(new StringBuffer().append("Cpu Percent=").append(CpuPerc.format(timer.getCpuUsage())).toString());
        } catch (SigarNotImplementedException e) {
        }
    }
}
