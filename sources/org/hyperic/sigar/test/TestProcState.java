package org.hyperic.sigar.test;

import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestProcState.class */
public class TestProcState extends SigarTestCase {
    public TestProcState(String name) {
        super(name);
    }

    private void traceState(Sigar sigar, long pid) {
        try {
            ProcState procState = sigar.getProcState(pid);
            traceln(new StringBuffer().append("[pid=").append(pid).append("] ").append(procState).toString());
        } catch (SigarException e) {
            traceln(new StringBuffer().append("pid ").append(pid).append(": ").append(e.getMessage()).toString());
        }
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        try {
            sigar.getProcState(getInvalidPid());
        } catch (SigarException e) {
        }
        ProcState procState = sigar.getProcState(sigar.getPid());
        traceState(sigar, sigar.getPid());
        char state = procState.getState();
        assertTrue(state == 'R' || state == 'S');
        assertTrue(procState.getName().indexOf("java") != -1);
        long[] pids = sigar.getProcList();
        for (long j : pids) {
            traceState(sigar, j);
        }
    }
}
