package org.hyperic.sigar.test;

import java.util.Date;
import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestProcTime.class */
public class TestProcTime extends SigarTestCase {
    public TestProcTime(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        try {
            sigar.getProcTime(getInvalidPid());
        } catch (SigarException e) {
        }
        ProcCpu procTime = sigar.getProcCpu(sigar.getPid());
        assertGtEqZeroTrace("StartTime", procTime.getStartTime());
        traceln(new StringBuffer().append("StartDate=").append(new Date(procTime.getStartTime())).toString());
        assertGtEqZeroTrace("User", procTime.getUser());
        assertGtEqZeroTrace("Sys", procTime.getSys());
        assertGtEqZeroTrace("Total", procTime.getTotal());
        double value = procTime.getPercent() * 100.0d;
        traceln(new StringBuffer().append("Percent=").append(value).toString());
        assertTrue(value >= 0.0d);
        int ncpu = sigar.getCpuList().length;
        assertTrue(value <= 100.0d * ((double) ncpu));
    }
}
