package org.hyperic.sigar.test;

import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestProcMem.class */
public class TestProcMem extends SigarTestCase {
    public TestProcMem(String name) {
        super(name);
    }

    private void traceMem(Sigar sigar, long pid) throws Exception {
        try {
            ProcMem procMem = sigar.getProcMem(pid);
            traceln(new StringBuffer().append("Pid=").append(pid).toString());
            traceln(new StringBuffer().append("Size=").append(Sigar.formatSize(procMem.getSize())).toString());
            traceln(new StringBuffer().append("Resident=").append(Sigar.formatSize(procMem.getResident())).toString());
            traceln(new StringBuffer().append("Share=").append(Sigar.formatSize(procMem.getShare())).toString());
            traceln(new StringBuffer().append("MinorFaults=").append(procMem.getMinorFaults()).toString());
            traceln(new StringBuffer().append("MajorFaults=").append(procMem.getMajorFaults()).toString());
            traceln(new StringBuffer().append("PageFaults=").append(procMem.getPageFaults()).toString());
        } catch (SigarException e) {
            traceln(new StringBuffer().append("pid ").append(pid).append(": ").append(e.getMessage()).toString());
        }
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        try {
            sigar.getProcMem(getInvalidPid());
        } catch (SigarException e) {
        }
        long[] pids = sigar.getProcList();
        for (long j : pids) {
            traceMem(sigar, j);
        }
    }
}
