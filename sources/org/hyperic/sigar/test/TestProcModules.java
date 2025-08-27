package org.hyperic.sigar.test;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestProcModules.class */
public class TestProcModules extends SigarTestCase {
    public TestProcModules(String name) {
        super(name);
    }

    private void printModules(Sigar sigar, long pid) throws SigarException {
        traceln(new StringBuffer().append("\npid=").append(pid).toString());
        try {
            List modules = sigar.getProcModules(pid);
            for (int i = 0; i < modules.size(); i++) {
                traceln(new StringBuffer().append(i).append(SymbolConstants.EQUAL_SYMBOL).append(modules.get(i)).toString());
            }
        } catch (SigarNotImplementedException e) {
        }
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        try {
            printModules(sigar, getInvalidPid());
        } catch (SigarException e) {
        }
        try {
            printModules(sigar, sigar.getPid());
            long[] pids = sigar.getProcList();
            for (int i = 0; i < pids.length; i++) {
                try {
                    printModules(sigar, pids[i]);
                } catch (SigarException e2) {
                    traceln(new StringBuffer().append(pids[i]).append(": ").append(e2.getMessage()).toString());
                }
            }
        } catch (SigarNotImplementedException e3) {
        }
    }
}
