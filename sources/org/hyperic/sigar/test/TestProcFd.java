package org.hyperic.sigar.test;

import java.io.File;
import java.io.FileInputStream;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarLoader;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.SigarPermissionDeniedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestProcFd.class */
public class TestProcFd extends SigarTestCase {
    static Class class$org$hyperic$sigar$Sigar;

    public TestProcFd(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Class clsClass$;
        Sigar sigar = getSigar();
        try {
            sigar.getProcFd(getInvalidPid());
        } catch (SigarException e) {
        }
        try {
            long pid = sigar.getPid();
            long total = sigar.getProcFd(pid).getTotal();
            if (class$org$hyperic$sigar$Sigar == null) {
                clsClass$ = class$("org.hyperic.sigar.Sigar");
                class$org$hyperic$sigar$Sigar = clsClass$;
            } else {
                clsClass$ = class$org$hyperic$sigar$Sigar;
            }
            SigarLoader loader = new SigarLoader(clsClass$);
            String path = loader.findJarPath(null);
            File file = new File(path, loader.getJarName());
            traceln(new StringBuffer().append("Opening ").append(file).toString());
            FileInputStream is = new FileInputStream(file);
            assertEqualsTrace("Total", total + 1, sigar.getProcFd(pid).getTotal());
            is.close();
            assertEqualsTrace("Total", total, sigar.getProcFd(pid).getTotal());
        } catch (SigarNotImplementedException e2) {
        } catch (SigarPermissionDeniedException e3) {
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
