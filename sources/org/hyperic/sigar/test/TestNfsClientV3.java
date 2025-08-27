package org.hyperic.sigar.test;

import org.hyperic.sigar.NfsClientV3;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestNfsClientV3.class */
public class TestNfsClientV3 extends SigarTestCase {
    public TestNfsClientV3(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        try {
            NfsClientV3 nfs = sigar.getNfsClientV3();
            traceMethods(nfs);
        } catch (SigarException e) {
        }
    }
}
