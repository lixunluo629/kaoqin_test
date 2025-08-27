package org.hyperic.sigar.test;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestProcArgs.class */
public class TestProcArgs extends SigarTestCase {
    static Class class$org$hyperic$sigar$test$TestProcArgs;

    public TestProcArgs(String name) {
        super(name);
    }

    private boolean findArg(String[] args, String what) {
        boolean found = false;
        traceln(new StringBuffer().append("find=").append(what).toString());
        for (int i = 0; i < args.length; i++) {
            traceln(new StringBuffer().append("   ").append(i).append(SymbolConstants.EQUAL_SYMBOL).append(args[i]).toString());
            if (args[i].equals(what)) {
                found = true;
            }
        }
        return found;
    }

    public void testCreate() throws Exception {
        Class clsClass$;
        Sigar sigar = getSigar();
        try {
            sigar.getProcArgs(getInvalidPid());
        } catch (SigarException e) {
        }
        try {
            String[] args = sigar.getProcArgs(sigar.getPid());
            if (getVerbose()) {
                if (class$org$hyperic$sigar$test$TestProcArgs == null) {
                    clsClass$ = class$("org.hyperic.sigar.test.TestProcArgs");
                    class$org$hyperic$sigar$test$TestProcArgs = clsClass$;
                } else {
                    clsClass$ = class$org$hyperic$sigar$test$TestProcArgs;
                }
                findArg(args, clsClass$.getName());
            }
            if (args.length > 0) {
                assertTrue(args[0].indexOf("java") != -1);
            }
            if (!System.getProperty("os.name").equals("HP-UX")) {
            }
        } catch (SigarNotImplementedException e2) {
        }
        long[] pids = sigar.getProcList();
        for (int i = 0; i < pids.length; i++) {
            String pidTrace = new StringBuffer().append("pid=").append(pids[i]).toString();
            try {
                String[] args2 = sigar.getProcArgs(pids[i]);
                traceln(pidTrace);
                for (int j = 0; j < args2.length; j++) {
                    traceln(new StringBuffer().append("   ").append(j).append("=>").append(args2[j]).append("<==").toString());
                }
            } catch (SigarException e3) {
                traceln(new StringBuffer().append(pidTrace).append(": ").append(e3.getMessage()).toString());
            }
        }
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }
}
