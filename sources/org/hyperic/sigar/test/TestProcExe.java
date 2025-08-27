package org.hyperic.sigar.test;

import java.io.File;
import junit.framework.AssertionFailedError;
import org.hyperic.sigar.ProcExe;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestProcExe.class */
public class TestProcExe extends SigarTestCase {
    public TestProcExe(String name) {
        super(name);
    }

    private void printExe(Sigar sigar, long pid) throws SigarException {
        traceln(new StringBuffer().append("\npid=").append(pid).toString());
        try {
            ProcExe exe = sigar.getProcExe(pid);
            String cwd = exe.getCwd();
            traceln(new StringBuffer().append("cwd='").append(cwd).append("'").toString());
            traceln(new StringBuffer().append("exe='").append(exe.getName()).append("'").toString());
        } catch (SigarNotImplementedException e) {
        }
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        try {
            sigar.getProcExe(getInvalidPid());
        } catch (SigarException e) {
        }
        try {
            ProcExe exe = sigar.getProcExe(sigar.getPid());
            File exeFile = new File(exe.getName());
            String cwd = exe.getCwd();
            traceln(new StringBuffer().append("cwd='").append(cwd).append("'").toString());
            if (cwd.length() > 0) {
                assertTrue(new File(cwd).isDirectory());
            }
            traceln(new StringBuffer().append("exe='").append(exe.getName()).append("'").toString());
            if (exe.getName().length() > 0) {
                assertTrue(exeFile.exists());
                assertTrue(exeFile.getName().startsWith("java"));
            }
        } catch (SigarNotImplementedException e2) {
        }
        long[] pids = sigar.getProcList();
        for (long j : pids) {
            try {
                printExe(sigar, j);
            } catch (AssertionFailedError e3) {
            } catch (SigarException e4) {
            }
        }
    }
}
