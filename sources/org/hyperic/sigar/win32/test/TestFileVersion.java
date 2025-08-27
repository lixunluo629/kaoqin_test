package org.hyperic.sigar.win32.test;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.test.SigarTestCase;
import org.hyperic.sigar.win32.FileVersion;
import org.hyperic.sigar.win32.Win32;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/test/TestFileVersion.class */
public class TestFileVersion extends SigarTestCase {
    public TestFileVersion(String name) {
        super(name);
    }

    private void printExe(long pid) {
        traceln(new StringBuffer().append("\npid=").append(pid).toString());
        try {
            String name = getSigar().getProcExe(pid).getName();
            FileVersion info = Win32.getFileVersion(name);
            if (info != null) {
                traceln(new StringBuffer().append("exe='").append(name).append("'").toString());
                traceln(new StringBuffer().append("version=").append(info.getProductVersion()).toString());
            }
        } catch (SigarException e) {
        }
    }

    public void testCreate() throws Exception {
        assertTrue(Win32.getFileVersion("DoEsNoTeXist.exe") == null);
        long[] pids = getSigar().getProcList();
        for (long j : pids) {
            printExe(j);
        }
    }
}
