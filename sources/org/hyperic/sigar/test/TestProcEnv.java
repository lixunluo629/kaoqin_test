package org.hyperic.sigar.test;

import java.io.File;
import java.util.Map;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.SigarPermissionDeniedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestProcEnv.class */
public class TestProcEnv extends SigarTestCase {
    public TestProcEnv(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        try {
            sigar.getProcEnv(getInvalidPid());
        } catch (SigarException e) {
        }
        long pid = sigar.getPid();
        try {
            Map env = sigar.getProcEnv(pid);
            traceln(env.toString());
            String val = (String) env.get("JAVA_HOME");
            String single = sigar.getProcEnv(pid, "JAVA_HOME");
            if (val != null) {
                assertTrue(new File(val, "bin").exists());
                assertTrue(val.equals(single));
                traceln(new StringBuffer().append("JAVA_HOME").append("==>").append(single).toString());
            }
            assertTrue(((String) env.get("dOeSnOtExIsT")) == null);
            assertTrue(sigar.getProcEnv(pid, "dOeSnOtExIsT") == null);
        } catch (SigarNotImplementedException e2) {
        } catch (SigarPermissionDeniedException e3) {
        }
        long[] pids = sigar.getProcList();
        for (long j : pids) {
            try {
                sigar.getProcEnv(j);
            } catch (SigarException e4) {
            }
        }
    }
}
