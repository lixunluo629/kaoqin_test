package org.hyperic.sigar.test;

import java.util.ArrayList;
import org.hyperic.sigar.Sigar;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestProcList.class */
public class TestProcList extends SigarTestCase {
    public TestProcList(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        ArrayList traceList = new ArrayList();
        long[] pids = sigar.getProcList();
        assertTrue(pids.length > 1);
        long pid = sigar.getPid();
        boolean foundPid = false;
        for (int i = 0; i < pids.length; i++) {
            traceList.add(new Long(pids[i]));
            if (pid == pids[i]) {
                foundPid = true;
            }
        }
        traceln(new StringBuffer().append("pids=").append(traceList).toString());
        assertTrue(foundPid);
    }
}
