package org.hyperic.sigar.test;

import org.hyperic.sigar.NfsClientV2;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestNfsClientV2.class */
public class TestNfsClientV2 extends SigarTestCase {
    public TestNfsClientV2(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        try {
            NfsClientV2 nfs = sigar.getNfsClientV2();
            traceMethods(nfs);
        } catch (SigarException e) {
        }
    }
}
